package com.md.md_voice.view.friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.md.md_voice.R;
import com.md.md_voice.view.mine.MineFragment;

public class FriendFragment extends Fragment {
    private Context mContext;
    private TextView mTextView;

    public FriendFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, null);
        mTextView = rootView.findViewById(R.id.textView);
        mTextView.setText("朋友");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //发请求更新UI
    }
}
