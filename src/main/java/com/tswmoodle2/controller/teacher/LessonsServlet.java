package com.tswmoodle2.controller.teacher;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import model.beans.Argomento;
import model.beans.Corso;
import model.beans.Lezione;
import model.dao.ArgomentoDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet("/lesson")
@MultipartConfig
public class LessonsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LessonsServlet.class.getName());

    private LezioneDaoImpl lezioneDao = new LezioneDaoImpl();
    private ArgomentoDaoImpl argomentoDao = new ArgomentoDaoImpl();
    private static final String SEPARATOR = "/";
    private String UPLOAD_FOLDER;

    private static final String LESSON_TEACHER_VIEW = "/WEB-INF/results/private/teacher/lessons.jsp";
    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    @Override
    public void init() {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");
        LOGGER.log(Level.INFO, "Upload folder set to: " + UPLOAD_FOLDER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        if(request.getParameter("lezione") != null) {
            Lezione lezione = lezioneDao.findById(Integer.parseInt(request.getParameter("lezione")));
            lezione.setArgomenti(argomentoDao.findAllByLezioneId(lezione.getId()));
            request.setAttribute("lezione", lezione);
        }

        switch (action) {
            case "new":
            case "display":
                handleNewOrDisplay(request, response);
                break;
            default:
                errorOccurs(request, response);
                break;
        }
    }

    private void handleNewOrDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdParam = request.getParameter("courseID");
        if (courseIdParam == null) {
            errorOccurs(request, response);
            return;
        }
        Corso corso = new CorsoDaoImpl().findByID(Integer.parseInt(courseIdParam));
        if (corso == null) {
            errorOccurs(request, response);
            return;
        } else {
            request.setAttribute("course", corso);
            request.getRequestDispatcher(LESSON_TEACHER_VIEW).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        LOGGER.log(Level.INFO, "Lessons action: {0}", action);

        switch (action) {
            case "createLesson":
                createLesson(request, response);
                break;
            case "createTopic":
                createTopic(request, response);
                break;
            case "deleteLesson":
                deleteLesson(request, response);
                break;
            case "deleteTopic":
                deleteTopic(request, response);
                break;
            default:
                errorOccurs(request, response);
                break;
        }
    }

    private void createLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("lessonTitle");
        String description = request.getParameter("lessonDescription");
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        String lessonID = request.getParameter("lessonID");
        Lezione lezione;

        if(lessonID != null) {
            lezione = lezioneDao.findById(Integer.parseInt(lessonID));
            if(lezione != null) {
                lezione.setTitolo(title);
                lezione.setDescrizione(description);
                lezioneDao.update(lezione);
            } else {
                errorOccurs(request, response);
                return;
            }
        } else {
            lezione = new Lezione();
            lezione.setTitolo(title);
            lezione.setDescrizione(description);
            lezione.setIdCorso(courseId);
            lezione.setId(lezioneDao.insertInto(lezione).getId());
        }

        response.sendRedirect("lesson?action=new&courseID=" + courseId + "&lezione=" + lezione.getId());
    }

    private void createTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "Creazione del topic");

        String title = request.getParameter("topicTitle");
        String description = request.getParameter("topicDescription");
        String lessonID = request.getParameter("lessonID");
        String topicId = request.getParameter("idargomento");
        String courseIdStr = request.getParameter("courseId");
        Part file = request.getPart("file");



        LOGGER.log(Level.INFO, "Parametri ricevuti: title = {0}, description = {1}, lessonID = {2}, topicId = {3}, courseIdStr = {4}",
                new Object[]{title, description, lessonID, topicId, courseIdStr});

        int lessonIdInt = Integer.parseInt(lessonID);
        int courseId = Integer.parseInt(courseIdStr);

        Argomento argomento;
        if (topicId != null && !topicId.isEmpty()) {
            argomento = argomentoDao.findById(Integer.parseInt(topicId));
            if (argomento != null) {
                argomento.setNome(title);
                argomento.setDescrizione(description);


                if (checkFile(file)) {
                    String fileName = getFileName(file);
                    LOGGER.log(Level.INFO, "File is not null and file name is valid");
                    argomento.setFilenames(new ArrayList<>(List.of(fileName)));
                    replaceFileIntoFolder(courseId, file, response, argomento.getId());
                }

                argomentoDao.update(argomento);
            } else {
                errorOccurs(request, response);
                return;
            }
        } else {
            argomento = new Argomento();
            argomento.setNome(title);
            argomento.setDescrizione(description);
            argomento.setIdLezione(lessonIdInt);
            argomento.setDataCaricamento(new Timestamp(System.currentTimeMillis()));
            LOGGER.log(Level.INFO, argomento.toString());

            if (checkFile(file)) {
                String fileName = getFileName(file);
                LOGGER.log(Level.INFO, "File is not null and file name is valid");
                argomento.setFilenames(new ArrayList<>(List.of(fileName)));
                replaceFileIntoFolder(courseId, file, response, -1);
            }

            argomentoDao.insertInto(argomento);
        }

        response.sendRedirect("lesson?action=new&courseID=" + courseId + "&lezione=" + lessonIdInt);
    }

    private void deleteTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topicId = request.getParameter("idargomento");

        if (topicId != null) {
            Argomento argomento = argomentoDao.findById(Integer.parseInt(topicId));
            if (argomento != null) {
                deleteTopicFile(Integer.parseInt(topicId), Integer.parseInt(request.getParameter("courseId")));
                argomentoDao.delete(Integer.parseInt(topicId));
                response.sendRedirect("lesson?action=display&courseID=" +
                        request.getParameter("courseId") + "&lezione=" +
                        request.getParameter("lessonID"));
            } else {
                errorOccurs(request, response);
            }
        } else {
            errorOccurs(request, response);
        }
    }

    private void deleteTopicFile(int topicID, int courseID) {
        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "course" + SEPARATOR + courseID;
        String fileToDeleteName = new ArgomentoDaoImpl().findFiles(topicID).get(0);
        String fileToDeletePath = uploadPath + SEPARATOR + fileToDeleteName;
        File deleteFile = new File(fileToDeletePath);
        LOGGER.log(Level.INFO, "Check if fileToDelete exists: {0}", deleteFile.exists());

        if(!checkIfFileIsUsedFromAnotherTopicOfCourse(topicID, courseID)) {
            if(fileToDeleteName != null) {
                if(!fileToDeleteName.isEmpty() && deleteFile.exists()) {
                    deleteFileFromFolder(deleteFile);
                }
            }
        }
    }

    private void replaceFileIntoFolder(int corsoID, Part filePart, HttpServletResponse response, int argomentoID) throws IOException {
        if (filePart == null) {
            LOGGER.log(Level.SEVERE, "No file uploaded or file part name mismatch.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded.");
            return;
        }

        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.log(Level.SEVERE, "File name could not be determined.");
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name could not be determined.");
            return;
        }

        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "course" + SEPARATOR + corsoID;
        LOGGER.log(Level.INFO, "Path for upload: {0}", uploadPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Path createdPath = Files.createDirectories(uploadDir.toPath());
                LOGGER.log(Level.INFO, "Directory created or already exists at: {0}", createdPath.toString());
                // Verifica esistenza directory
                if (Files.exists(createdPath)) {
                    LOGGER.log(Level.INFO, "Confirmed: Directory exists at: {0}", createdPath.toString());
                } else {
                    LOGGER.log(Level.SEVERE, "Directory creation failed: {0}", createdPath.toString());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                    return;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create directory: " + e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                return;
            }
        }

        String filePath = uploadPath + SEPARATOR + fileName;
        LOGGER.log(Level.INFO, "File path: {0}", filePath);

        // Check if the file already exists
        File existingFile = new File(filePath);
        if (existingFile.exists()) {
            LOGGER.log(Level.INFO, "File already exists: {0}", filePath);
            // Option 1: Delete the existing file

        } else {
            if(argomentoID > 0)
                deleteTopicFile(argomentoID, corsoID);

            try (InputStream input = filePart.getInputStream()) {

                Files.copy(input, Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Failed to save uploaded file: " + ex.getMessage(), ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
                return;
            }
        }

    }

    private boolean checkIfFileIsUsedFromAnotherTopicOfCourse(int topicid, int courseid) {
        String fileToCheck = new ArgomentoDaoImpl().findFiles(topicid).get(0);

        return new CorsoDaoImpl().countFileUsage(topicid, courseid, fileToCheck) > 0;
    }

    private void deleteFileFromFolder(File deleteFile) {
        try {
            // Chiudere eventuali risorse ancora aperte
            closeFileResources(deleteFile);

            // Attendi prima di tentare di eliminare
            try {
                Thread.sleep(100); // Attendi 100 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Thread interrupted", e);
            }

            // Tentativo di eliminazione del file
            if (deleteFile.delete()) {
                LOGGER.log(Level.INFO, "Previous file successfully eliminated");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete the previous file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting the previous file", e);
            throw new RuntimeException(e);
        }
    }

    private void closeFileResources(File file) throws IOException {
        System.gc();

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock()) {
            // Forzare il rilascio del lock
            lock.release();
            LOGGER.log(Level.INFO, "File resources successfully closed for: {0}", file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing file resources for: {0} {1}", new Object[]{file.getAbsolutePath(), e});
            throw e;
        }
    }


    private void deleteLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonID = request.getParameter("lessonID");

        if (lessonID != null) {
            Lezione lezione = lezioneDao.findById(Integer.parseInt(lessonID));
            if (lezione != null) {
                for(Argomento arg : new ArgomentoDaoImpl().findAllByLezioneId(Integer.parseInt(lessonID))) {
                    deleteTopicFile(arg.getId(), lezione.getIdCorso());
                }
                lezioneDao.delete(Integer.parseInt(lessonID));
                response.sendRedirect("lesson?action=new&courseID=" + lezione.getIdCorso());
            } else {
                errorOccurs(request, response);
            }
        } else {
            errorOccurs(request, response);
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return null;
        }
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.isEmpty() ? null : fileName;
            }
        }
        return null;
    }

    private boolean checkFile(Part file) {
        String fileName = getFileName(file);
        LOGGER.log(Level.INFO, "File name: {0}", fileName);
        return file != null && fileName != null && !fileName.isEmpty();
    }


    private void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("Errore generico");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }
}
