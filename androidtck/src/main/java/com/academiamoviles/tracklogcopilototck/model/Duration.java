package com.academiamoviles.tracklogcopilototck.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by veraj on 5/08/2018.
 */

public class Duration {
    List<Date>  listDate = new ArrayList<>();


    public List<Date> getListDate() {
        return listDate;
    }

    public void setListDate(List<Date> listDate) {
        this.listDate = listDate;
    }
}
