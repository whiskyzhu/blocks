package com.blocks.framework.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lotus on 2016/12/20.
 */
public class DatasetList extends ArrayList<Object> implements IDataset {

    private static final long serialVersionUID = -2772591905292799139L;

    public DatasetList() {
        super();
    }

    /**
     * construct function
     *
     * @param data
     */
    public DatasetList(IData data) {
        super();
        add(data);
    }

    /**
     * construct function
     *
     * @param datas
     */
    public DatasetList(IData[] datas) {
        super();
        for (IData data : datas) {
            add(data);
        }
    }

    /**
     * construct function
     *
     * @param list
     */
    public DatasetList(IDataset list) {
        super();
        addAll(list);
    }

    /**
     * construct function
     *
     * @param jsonArray
     */
    public DatasetList(String jsonArray) {
        super();
        JSONArray array = JSONObject.parseArray(jsonArray);
        addAll(DatasetList.fromJSONArray(array));
    }

    /**
     * construct function
     *
     * @param array
     */
    public DatasetList(JSONArray array) {
        super();
        this.addAll(DatasetList.fromJSONArray(array));
    }

    public static DatasetList fromJSONArray(JSONArray array) {
        if (array != null) {
            DatasetList list = new DatasetList();
            Iterator<?> it = array.iterator();
            while (it.hasNext()) {
                Object value = it.next();
                if (value != null) {
                    if (value instanceof JSONObject) {
                        list.add(DataMap.fromJSONObject((JSONObject) value));
                    } else if (value instanceof DataMap) {
                        list.add((IData) value);
                    }else if (value instanceof String) {
                        if (mayBeJSON((String) value)) {
                            if (((String) value).startsWith("{")) {
                                list.add(new DataMap(((String) value)));
                            } else if (((String) value).startsWith("[")) {
                                list.add(new DatasetList(((String) value)));
                            } else {
                                list.add(value);
                            }
                        }
                    }else{
                        list.add(value);
                    }
                } else {
                    list.add(null);
                }
            }
            return list;
        }
        return null;
    }

    /**
     * get names
     *
     * @return String[]
     */
    public String[] getNames() {
        return size() > 0 ? ((IData) get(0)).getNames() : null;
    }

    public Object get(int index){
        return super.get(index);
    }

    /**
     * get object
     * @param index
     * @param name
     * @return Object
     */
    public Object get(int index, String name) {
        IData data = (IData) get(index);
        return data == null ? null : data.get(name);
    }

    public Object get(int index, String name, Object def) {
        Object value = get(index, name);
        return value == null ? def : value;
    }

    /**
     * get data
     * @param index
     * @return IData
     */
    @SuppressWarnings("unchecked")
    public IData getData(int index){
        Object value = get(index);
        if (value == null)
            return null;
        if (value instanceof String) {
            return new DataMap((String)value);
        } else if (value instanceof JSONObject) {
            IData data = new DataMap();
            data.putAll((JSONObject) value );
            return data;
        }else{
            return (IData)value;
        }
    }

    /**
     * get dataset
     * @param index
     * @return IDataset
     */
    public IDataset getDataset(int index) {
        Object value = get(index);
        if (value == null)
            return null;

        if (value instanceof String) {
            return new DatasetList((String)value);
        } else if (value instanceof JSONArray) {
            return DatasetList.fromJSONArray((JSONArray)value);
        }else{
            return (IDataset)value;
        }
    }

    /**
     * first
     *
     * @return IData
     */
    public IData first() {
        return size() > 0 ? (IData)get(0) : null;
    }

    /**
     * to data
     * @return IData
     */
    public IData toData() {
        IData data = new DataMap();

        Iterator<Object> it = iterator();
        while (it.hasNext()) {
            IData element = (IData) it.next();
            Iterator<String> iterator = element.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (data.containsKey(key)) {
                    IDataset list = (IDataset) data.get(key);
                    list.add(element.get(key));
                } else {
                    IDataset list = new DatasetList();
                    list.add(element.get(key));
                    data.put(key, list);
                }
            }
        }

        return data;
    }

    /**
     * to string
     * @return String
     */
    public String toString() {
        return JSONArray.toJSONString(this).toString();
    }


    public static boolean mayBeJSON(String string)
    {
        return string != null && ("null".equals(string) || string.startsWith("[") && string.endsWith("]") || string.startsWith("{") && string.endsWith("}"));
    }


    /**
     * to XML string
     * @return XMLString
     */
    public String toXMLString(){
        StringBuilder strBuilder = new StringBuilder();
        Iterator<Object> it = iterator();
        while (it.hasNext()) {
            Object value = it.next();
            if(value!=null){
                strBuilder.append("<item>");
                if(value instanceof String){
                    strBuilder.append((String)value);
                }else if(value instanceof IData){
                    strBuilder.append(((IData)value).toXMLString());
                }else if(value instanceof IDataset){
                    strBuilder.append(((IDataset)value).toXMLString());
                }else{
                    strBuilder.append(value);
                }
                strBuilder.append("</item>");
            }
        }
        return strBuilder.toString();
    }

    /**
     * to XML string
     * @return XMLString
     */
    public String toXMLString(String root){
        if(StringUtils.isEmpty(root))
            root = "root";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<");
        strBuilder.append(root);
        strBuilder.append(">");
        strBuilder.append(this.toXMLString());
        strBuilder.append("</");
        strBuilder.append(root);
        strBuilder.append(">");

        return strBuilder.toString();
    }
}
