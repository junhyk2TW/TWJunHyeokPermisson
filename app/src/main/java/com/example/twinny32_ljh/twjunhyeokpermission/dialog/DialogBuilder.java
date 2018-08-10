package com.example.twinny32_ljh.twjunhyeokpermission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.twinny32_ljh.twjunhyeokpermission.R;

public abstract class DialogBuilder<T extends DialogBuilder> extends Dialog{


    private Context mContext;
    private Dialog mDialog;
    private View mDialogView;


    public DialogBuilder(@NonNull Context context) {
        super(context);
        this.mContext=context;
    }

    public DialogBuilder(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;

    }

    protected DialogBuilder(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext=context;

    }


    public void show(){
        mDialog = this.createDialog();
        mDialog.show();
    }

    public T createDialog(){
        // 다이얼로그에 들어갈 뷰정보를 셋팅한다.
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogView = inflater.inflate(R.layout.dialog_custom, null);
        final DialogBuilder dialogBuilder =null;

        return (T)this;
    }

//    public T create()
//    {
//        // 다이얼로그에 들어갈 뷰정보를 셋팅한다.
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mDialogView = inflater.inflate(R.layout.dialog_schedule_more, null);
//
//        // 다이얼로그 생성
//        final TWSelectDialog dialog = new TWSelectDialog(mContext, R.style.Dialog);
//        // 다이얼로그에 뷰 삽입
//        dialog.setContentView(mDialogView);
//
//        if(mTitle != null && mItem1 != null && mItem2 != null && mItem3 != null)
//        {
//            ((TextView) mDialogView.findViewById(R.id.tv_title)).setText(mTitle);
//            ((Button) mDialogView.findViewById(R.id.btn_dialog_option01)).setText(mItem1);
//            ((Button) mDialogView.findViewById(R.id.btn_dialog_option02)).setText(mItem2);
//            ((Button) mDialogView.findViewById(R.id.btn_dialog_option03)).setText(mItem3);
//            ((Button) mDialogView.findViewById(R.id.btn_dialog_option03)).setVisibility(View.VISIBLE);
//        }
//        mDialogView.findViewById(R.id.btn_dialog_option01).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // 수정하기
//                if (checkClickableTime() && mListener1 != null)
//                {
//                    mListener1.onClick(dialog, 0);
//                }
//            }
//        });
//
//        mDialogView.findViewById(R.id.btn_dialog_option02).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // 삭제하기
//                if (checkClickableTime() && mListener2 != null)
//                {
//                    mListener2.onClick(dialog, 1);
//                }
//            }
//        });
//
//        mDialogView.findViewById(R.id.btn_dialog_option03).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // 삭제하기
//                if (checkClickableTime() && mListener3 != null)
//                {
//                    mListener3.onClick(dialog, 2);
//                }
//            }
//        });
//        return T;
//    }
}
