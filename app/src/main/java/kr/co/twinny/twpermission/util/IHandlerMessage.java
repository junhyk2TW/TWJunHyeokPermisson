/**
 * @namespace kr.co.twinny.moigo.util
 * @brief 모이고 앱의 유틸리티 패키지
 */
package kr.co.twinny.twpermission.util;

import android.os.Message;

/**
 * @brief 핸들러 메시지 전달 인터페이스
 */
public interface IHandlerMessage
{
    void handlerMessage(Message msg);
}
