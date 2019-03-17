package com.skynet.lian.ui.tabcall;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.CallItemModel;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.views.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TabCallFragment extends BaseFragment implements ICallback {

    @BindView(R2.id.imgHome)
    CircleImageView imgHome;
    @BindView(R2.id.imgStausToolbar)
    ImageView imgStausToolbar;
    @BindView(R2.id.tvNameToolbar)
    TextView tvNameToolbar;

    @BindView(R2.id.imgMore)
    ImageView imgMore;
    @BindView(R2.id.imgSearch)
    ImageView imgSearch;
    @BindView(R2.id.view)
    View view;

    @BindView(R2.id.textView7)
    TextView textView7;
    @BindView(R2.id.view2)
    View view2;
    @BindView(R2.id.rcvChatItem)
    RecyclerView rcvChatItem;

    @BindView(R2.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R2.id.swipeTabChat)
    SwipeRefreshLayout swipeTabChat;

    @BindView(R2.id.imgAddNewMessage)
    ImageButton imgAddNewMessage;
    @BindView(R2.id.layoutToolbar)
    ConstraintLayout layoutToolbar;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.layoutSearch)
    ConstraintLayout layoutSearch;

    private List<CallItemModel> list;
    private AdapterCallItem adapterChatItem;

    public static TabCallFragment newInstance() {

        Bundle args = new Bundle();

        TabCallFragment fragment = new TabCallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void doAction() {

    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_tab_call;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        rcvChatItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvChatItem.setHasFixedSize(true);
//        rcvChatItem.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        tvNameToolbar.setText("Gọi điện");
        imgMore.setVisibility(View.GONE);


    }

    @Override
    protected void initVariables() {
        list = new ArrayList<>();

        adapterChatItem = new AdapterCallItem(getContext(), list);
        //This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Hôm nay"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(3, "Hôm qua"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(6, "21 Tháng 12, 2018"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(9, "21 Tháng 12, 2018"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(13, "21 Tháng 12, 2018"));

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getContext(), R.layout.section, R.id.section_text, adapterChatItem);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        rcvChatItem.setAdapter(mSectionedAdapter);

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//                list.add(new CallItemModel());
//
//
//                adapterChatItem.notifyDataSetChanged();
//            }
//        });
    }

    @OnClick({R2.id.imgHome, R2.id.tvNameToolbar, R2.id.imgMore, R2.id.imgBack,
            R2.id.imgSearch, R2.id.imgAddNewMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R2.id.imgHome:
                break;

            case R2.id.tvNameToolbar:
                break;

            case R2.id.imgMore:
                break;
            case R2.id.imgBack:
                layoutToolbar.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.INVISIBLE);
                break;
            case R2.id.imgSearch:
                layoutToolbar.setVisibility(View.INVISIBLE);
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            case R2.id.imgAddNewMessage:
                break;
        }
    }

    @Override
    public void onCallBack(int pos) {

    }
}
