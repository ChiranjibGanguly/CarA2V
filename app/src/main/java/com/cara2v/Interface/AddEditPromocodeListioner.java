package com.cara2v.Interface;

import com.cara2v.responceBean.GetAllPromoCodeResponce;

/**
 * Created by chiranjib on 23/3/18.
 */

public interface AddEditPromocodeListioner {
    void onEdit(GetAllPromoCodeResponce.DataBean dataBean);

    void onDelete(GetAllPromoCodeResponce.DataBean dataBean);
}
