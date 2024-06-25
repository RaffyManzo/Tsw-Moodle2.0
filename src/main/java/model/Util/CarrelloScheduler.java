package model.Util;

import model.beans.Carrello;

import java.util.Timer;
import java.util.TimerTask;

public class CarrelloScheduler {
    private final Timer timer = new Timer(true);
    private final CarrelloService carrelloService;
    private final Carrello carrello;

    public CarrelloScheduler(CarrelloService carrelloService, Carrello carrello) {
        this.carrelloService = carrelloService;
        this.carrello = carrello;
    }

    public void startScheduler(long delay, long period) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                carrelloService.saveCarrello(carrello);
            }
        }, delay, period);
    }

    public void stopScheduler() {
        timer.cancel();
    }
}

