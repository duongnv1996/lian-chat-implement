package com.skynet.lian.ui.tabgroup;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class TabGroupPresenter extends Presenter<TabGoupContract.View> implements TabGoupContract.Presenter {
    TabGoupContract.Interactor interactor;

    public TabGroupPresenter(TabGoupContract.View view) {
        super(view);
        interactor = new TabGroupImplRemote(this);
    }


    @Override
    public void getListChat(String query) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListChat(query);
        }
    }

    @Override
    public void getListChat() {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListChat();
        }
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorApi(message);
        }
    }

    @Override
    public void deleteChat(String id) {
        if (isAvaliableView()) {
            interactor.deleteChat(id
            );
        }
    }

    @Override
    public void onError(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onError(message);
        }
    }

    @Override
    public void onErrorAuthorization() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorAuthorization();
        }
    }


    @Override
    public void onSucessListChat(List<ChatItem> list) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessListChat(list);
        }
    }
}
