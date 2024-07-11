document.addEventListener('DOMContentLoaded', () => {
    const courseId = document.getElementById('courseId').value;
    console.log(courseId)

    // Carica le lezioni salvate dallo storage locale
    // loadStoredLessons(courseId);

    initializeAccordions();
    initializeForms(courseId);

    document.getElementById('createLessonButton').addEventListener('click', function () {
        document.getElementById('createLessonButton').disabled = true;
        createLesson(courseId);
    });

    document.querySelector("#delete-lesson").addEventListener("click", function () {
        document.querySelector("#action").value = "deleteLesson";
        document.querySelector("#form-on-lesson").submit();
    });

    document.querySelectorAll(".delete-topic").forEach(el => {
        el.addEventListener("click", function () {
            var form = el.parentNode.parentNode.parentNode
            form.querySelector("input[name='action']").value = "deleteTopic";
            form.method = "post";
            form.submit()
        })
    })
});

function initializeAccordions() {
    var acc = document.getElementsByClassName("accordion");
    for (let i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function () {
            this.classList.toggle("active");
            var panel =  this.closest('.lesson-form').querySelector('.panel');
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
                panel.style.padding = "0 15px";
            } else {
                panel.style.maxHeight = "fit-content";
                panel.style.padding = "15px";
            }
        });
    }
}

function initializeForms(courseId) {
    var forms = document.querySelectorAll('.lesson-form');
    forms.forEach(form => {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            addHiddenInputsToForm(form, courseId);


            this.submit();
        });

        var saveButton = form.closest('.save-topic');
        var deleteButton = form.closest('.delete-lesson');

        saveButton?.addEventListener('click', function () {
            form.querySelector('input[name="action"]').value = 'createTopic';
            form.submit();
        });

        deleteButton?.addEventListener('click', function () {
            form.querySelector('input[name="action"]').value = 'deleteTopic';
            form.submit();
        });
    });
}

function addHiddenInputsToForm(form, courseId) {
    var inputCourseId = document.createElement('input');
    inputCourseId.type = 'hidden';
    inputCourseId.name = 'courseId';
    inputCourseId.value = courseId;
    form.appendChild(inputCourseId);

    var inputLessonId = document.createElement('input');
    inputLessonId.type = 'hidden';
    inputLessonId.name = 'lessonId';
    inputLessonId.value = document.getElementById("lessonid").value;
    form.appendChild(inputLessonId);
}


function createLesson(courseId) {
    var form = document.createElement('form');
    form.className = "lesson-form";
    form.method = "post";
    form.enctype = "multipart/form-data"
    form.action = "lesson";

    var accordion = document.createElement('div');
    accordion.className = "accordion";

    var inputCourseId = document.createElement('input');
    inputCourseId.type = "hidden";
    inputCourseId.name = "courseId";
    inputCourseId.value = courseId;
    accordion.appendChild(inputCourseId);

    var inputLessonId = document.createElement('input');
    inputLessonId.type = "hidden";
    inputLessonId.name = "lessonID";
    inputLessonId.value = document.getElementById("lessonid").value;
    accordion.appendChild(inputLessonId);

    var inputAction = document.createElement('input');
    inputAction.type = "hidden";
    inputAction.name = "action";
    inputAction.value = "createTopic";
    accordion.appendChild(inputAction);

    var inputTitle = document.createElement('input');
    inputTitle.type = "text";
    inputTitle.name = "topicTitle";
    inputTitle.placeholder = "Inserisci il titolo dell'argomento";
    accordion.appendChild(inputTitle);


    form.appendChild(accordion);
    addHiddenInputsToForm(form , courseId)

    var panel = document.createElement('div');
    panel.className = "panel";

    var textarea = document.createElement('textarea');
    textarea.name = "topicDescription";
    textarea.placeholder = "Inserisci la descrizione dell'argomento";
    panel.appendChild(textarea);

    var uploadDiv = document.createElement("div");
    uploadDiv.className = "file-upload";
    uploadDiv.addEventListener("click", function () {
        this.querySelector('.fileInput').click();
    })
    panel.appendChild(uploadDiv);

    var fileInput = document.createElement("input");
    fileInput.className = "fileInput";
    fileInput.type = "file";
    fileInput.name = "file"
    fileInput.accept = ".pdf,.txt";

    fileInput.addEventListener("change", function () {
            const fileDetailsContainer = this.parentNode.parentNode.querySelector('.fileDetailsContainer');
            fileDetailsContainer.innerHTML = ''; // Clear any existing file details

            const files = fileInput.files;
            if (files.length > 0) {
                const file = files[0];
                const fileDetails = document.createElement('div');
                fileDetails.classList.add('file-details');

                const fileIcon = document.createElement('span');
                fileIcon.classList.add('file-icon');
                fileIcon.innerHTML = "<img src=\"${pageContext.request.contextPath}/assets/images/file-text.png\" alt=\"📄\">"; // File icon
                p.innerText = "Scegli un altro file"

                const fileName = document.createElement('span');
                fileName.textContent = `${file.name} (${file.type})`;

                fileDetails.appendChild(fileIcon);
                fileDetails.appendChild(fileName);
                fileDetailsContainer.appendChild(fileDetails);
            }

    })

    uploadDiv.appendChild(fileInput);

    var p = document.createElement("p")
    p.innerText = "Scegli un file da caricare"

    uploadDiv.appendChild(p);

    var preview = document.createElement("div");
    preview.className = "fileDetailsContainer";

    panel.appendChild(preview);



    var buttonContainer = document.createElement('div');
    buttonContainer.className = "button-container";

    var saveButton = document.createElement('button');
    saveButton.type = "submit";
    saveButton.className = "save-topic";
    saveButton.textContent = "Salva modifiche";
    buttonContainer.appendChild(saveButton);


    panel.appendChild(buttonContainer);
    form.appendChild(panel);

    var lessonsWrapper = document.querySelector('.lessons-wrapper');
    lessonsWrapper.appendChild(form);

    initializeAccordions();
    initializeForms(courseId);

}

function displayFileDetails(inputElem) {
    const fileDetailsContainer = inputElem.parentNode.parentNode.querySelector('.fileDetailsContainer');
    fileDetailsContainer.innerHTML = ''; // Clear any existing file details

    const files = inputElem.files;
    if (files.length > 0) {
        const file = files[0];
        const fileDetails = document.createElement('div');
        fileDetails.classList.add('file-details');

        const fileIcon = document.createElement('span');
        fileIcon.classList.add('file-icon');
        fileIcon.innerHTML = "<img src=\"${pageContext.request.contextPath}/assets/images/file-text.png\" alt=\"📄\">"; // File icon

        const fileName = document.createElement('span');
        fileName.textContent = `${file.name} (${file.type})`;

        fileDetails.appendChild(fileIcon);
        fileDetails.appendChild(fileName);
        fileDetailsContainer.appendChild(fileDetails);
    }
}
