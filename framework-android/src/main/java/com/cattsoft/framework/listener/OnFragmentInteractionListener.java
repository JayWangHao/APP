package com.cattsoft.framework.listener;

/**
 * Fragment与activity交互的Listener，用户从Fragment向Activity传递数据
 *
 * Created by Baixd on 15/5/26.
 */
public interface OnFragmentInteractionListener<T> {

    public void onFragmentInteraction(T t);

}
