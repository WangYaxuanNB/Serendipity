package com.sum.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sum.annotations.ViewHeader;
import com.sum.base.BaseQuery;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;
import com.sum.base.SelectionModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TableView工具类
 */
@Component
public class TableViewUtil {

    /**
     * 创建分页tableview
     *
     * @param wrapper
     * @param service
     * @param baseQuery
     * @param t
     * @param selectionModel
     * @param <T>
     * @return
     */
    public <T> Pagination createPagination(LambdaQueryWrapper<T> wrapper, IService<T> service, BaseQuery baseQuery, T t, SelectionModel<T> selectionModel) {
        int total = service.count(wrapper);
        baseQuery.setTotal(total);
        Pagination pagination = new Pagination(baseQuery.pageCount(), 0);
        pagination.setPageFactory(currentPage -> queryPage(currentPage, baseQuery, wrapper, service, t, selectionModel));
        return pagination;
    }

    /**
     * 创建tableview
     *
     * @param currentPage
     * @param baseQuery
     * @param wrapper
     * @param service
     * @param t
     * @param selectionModel
     * @param <T>
     * @return
     */
    public <T> Node queryPage(Integer currentPage, BaseQuery baseQuery, LambdaQueryWrapper<T> wrapper, IService<T> service, T t, SelectionModel<T> selectionModel) {
        baseQuery.setCurrentPage(currentPage + 1);
        List<T> dataList = new ArrayList<>();
        Page<T> page = service.page(baseQuery.getPage(), wrapper);
        if (ObjectUtil.isNotNull(page) && CollectionUtil.isNotEmpty(page.getRecords())) {
            dataList = page.getRecords();
        }
        TableView<T> tableView = this.createTableView(t, dataList, selectionModel);
        tableView.setCursor(Cursor.HAND);
        return tableView;
    }

    public <T> TableView<T> createTableView(T t, List<T> dataList, SelectionModel<T> selectionModel) {
        TableView<T> tableView = new TableView<>();
        tableView.setItems(FXCollections.observableArrayList(dataList));
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ViewHeader viewHeader = field.getAnnotation(ViewHeader.class);
            if (null != viewHeader) {
                TableColumn<T, String> column = new TableColumn<>(viewHeader.value());
                /*if (t instanceof Comments) {
                    tableView.setRowFactory(tv -> {
                        TableRow<T> row = new TableRow<>();
                        row.setPrefHeight(80);
                        return row;
                    });
                    if (StringUtils.equals(field.getName(), "blogTitle")) {
                        column.setPrefWidth(220);
                    } else if (StringUtils.equals(field.getName(), "content")) {
                        column.setPrefWidth(400);
                    } else {
                        column.setPrefWidth(140);
                    }
                } else {
                    column.setPrefWidth(140);
                }*/
                column.setPrefWidth(140);
                column.setStyle("-fx-wrap-text:true;cursor:hand;");
                column.setSortable(false);
                column.setResizable(false);
                column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
                tableView.getColumns().add(column);
            }
        }
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setPrefHeight(40);
            return row;
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectionModel.setValue(newVal);
            }
        });
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return tableView;
    }
}
