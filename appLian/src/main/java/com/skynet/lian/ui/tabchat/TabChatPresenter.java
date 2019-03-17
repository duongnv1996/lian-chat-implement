package com.skynet.lian.ui.tabchat;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class TabChatPresenter extends Presenter<TabChatContract.View> implements TabChatContract.Presenter {
    TabChatContract.Interactor interactor;

    public TabChatPresenter(TabChatContract.View view) {
        super(view);
        interactor = new TabChatImplRemote(this);
    }

    @Override
    public void getListFriendOnline() {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListFriendOnline();
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
    public void getListChat(String query) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getListChat(query);
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
    public void onSucessListFriendOnline(List<Profile> list) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onSucessListFriendOnline(list);
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
