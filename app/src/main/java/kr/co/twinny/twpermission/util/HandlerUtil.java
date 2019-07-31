/**
 * @namespace kr.co.twinny.moigo.util
 * @brief 모이고 앱의 유틸리티 패키지
 */
package kr.co.twinny.twpermission.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @brief 핸들러 클래스 (메모리 릭 방지용 커스텀 클래스)
 */
public class HandlerUtil extends Handler
{
    private WeakReference<IHandlerMessage> mHandlerInterface;

    public HandlerUtil(IHandlerMessage handlerInterface)
    {
        mHandlerInterface = new WeakReference<>(handlerInterface);
    }

    public void clear()
    {
        if (mHandlerInterface != null)
        {
            mHandlerInterface.clear();
            mHandlerInterface = null;
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        if (mHandlerInterface == null)
        {
            return;
        }

        IHandlerMessage handlerInterface = mHandlerInterface.get();

        if (handlerInterface == null)
        {
            return;
        }

        handlerInterface.handlerMessage(msg);
    }
}
