package project.controller;

import project.view.MainView;
import project.view.OperationManagerView;

public class OperationManagerController {

    private final OperationManagerView operationManagerView;

    public OperationManagerController(MainView mainView) {
        operationManagerView = new OperationManagerView();
        mainView.setContent(operationManagerView);
    }

}