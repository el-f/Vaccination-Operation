package project.controller;

import project.model.entities.Worker;
import project.view.MainView;
import project.view.WorkerView;

public class WorkerController {

    private final Worker workerUser;
    private final WorkerView workerView;

    public WorkerController(Worker _workerUser, MainView mainView) {
        workerUser = _workerUser;
        workerView = new WorkerView();
        mainView.setContent(workerView);
    }

}
