package project.controller;

import project.model.EntitiesManager;
import project.view.MainView;
import project.view.ViewUtils;
import project.view.operation_manager.OperationManagerView;

public class OperationManagerController {

    private final OperationManagerView operationManagerView;
    private final MainView mainView;

    public OperationManagerController(MainView _mainView) {
        operationManagerView = new OperationManagerView();
        mainView = _mainView;
        mainView.setContent(operationManagerView);

        operationManagerView.appointmentsSetOnClick(click -> {});
        operationManagerView.vaccinationsSetOnClick(click -> {});
        operationManagerView.suppliesSetOnClick(click -> {});
        operationManagerView.clinicsSetOnClick(click -> {});
        operationManagerView.workersSetOnClick(click -> operationManagerView.setCenter(
                ViewUtils.getWorkerTableView(EntitiesManager.instance().getAllWorkers())
        ));
        operationManagerView.citizensSetOnClick(click -> {});
    }

}
