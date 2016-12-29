package com.blocks.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lotus on 2016/12/20.
 */
public class DataMap extends HashMap<String, Object> implements IData {

    private static final long serialVersionUID = -7650119818937998364L;

    public DataMap() {
        super();
    }

    public DataMap(Map<String, Object> map) {
        super(map);
    }

    public DataMap(String jsonObject) {
        JSONObject map = JSONObject.parseObject(jsonObject);
        if (map != null) {
            this.putAll(fromJSONObject(map));
        }
    }

    public static DataMap fromJSONObject(JSONObject object) {
        if (object != null) {
            DataMap data = new DataMap();
            Iterator<?> keys = object.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                Object value = object.get(key);

                if (value != null) {
                    if (value instanceof JSONObject) {
                        data.put((String) key, DataMap.fromJSONObject((JSONObject) value));
                    } else if (value instanceof JSONArray) {
                        data.put((String) key, DatasetList.fromJSONArray((JSONArray) value));
                    } else if (value instanceof String) {
						/*
						 * if(JSONUtils.mayBeJSON((String)value)){
						 * if(((String)value).startsWith("{")){
						 * data.put((String)key, new DataMap(((String)value)));
						 * }else if(((String)value).startsWith("[")){
						 * data.put((String)key, new
						 * DatasetList(((String)value))); }else{
						 * data.put((String)key, value); } }else{
						 */
                        data.put((String) key, value);
                        // }
                    } else {
                        data.put((String) key, value);
                    }
                } else {
                    data.put((String) key, value);
                }
            }
            return data;
        }
        return null;
    }

    /**
     * get names
     * @return String[]
     */
    public String[] getNames() {
        String[] names = new String[size()];
        Iterator<String> keys = keySet().iterator();
        int index = 0;
        while(keys.hasNext()) {
            names[index] = keys.next();
            index++;
        }
        return names;
    }

    /**
     * name is null or not exist
     * @param name
     * @return boolean
     */
    public boolean isNoN(String name) {
        return name == null || !containsKey(name);
    }

    /**
     * get
     * @param name
     * @return boolean
     */
    public String getString(String name) {
        Object value = super.get(name);
        if (value == null) return null;
        return value.toString();
    }

    /**
     * get string
     * @param name
     * @param defaultValue
     * @return String
     */
    public String getString(String name, String defaultValue) {
        String value = getString(name);
        if (value == null) return defaultValue;
        return value;
    }

    /**
     * get boolean
     * @param name
     * @return boolean
     */
    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * get boolean
     * @param name
     * @param defaultValue
     * @return boolean
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        Object value = get(name);
        if (null == value)
            return defaultValue;

        return "true".equalsIgnoreCase(value.toString());
    }

    /**
     * get double
     * @param name
     * @return double
     */
    public double getDouble(String name) {
        return getDouble(name, 0);
    }

    /**
     * get double
     * @param name
     * @param defaultValue
     * @return double
     */
    public double getDouble(String name, double defaultValue) {
        Object value = super.get(name);
        if (value == null || StringUtils.isBlank(value.toString())) return defaultValue;

        return Double.parseDouble(value.toString());
    }

    /**
     * get int
     * @param name
     * @return int
     */
    public int getInt(String name) {
        return getInt(name, 0);
    }

    /**
     * get int
     * @param name
     * @param defaultValue
     * @return int
     */
    public int getInt(String name, int defaultValue) {
        Object value = super.get(name);
        if (value == null || StringUtils.isBlank(value.toString()))return defaultValue;
        return Integer.parseInt(value.toString());
    }

    /**
     * get long
     * @param name
     * @return long
     */
    public long getLong(String name) {
        return getLong(name, 0);
    }

    /**
     * get long
     * @param name
     * @param defaultValue
     * @return long
     */
    public long getLong(String name, long defaultValue) {
        Object value = super.get(name);
        if (value == null || StringUtils.isBlank(value.toString())) return defaultValue;
        return Long.parseLong(value.toString());
    }

    /**
     * get data
     * @param name
     * @return IData
     */
    public IData getData(String name) {
        Object value = super.get(name);
        if (value == null)
            return null;

        if (value instanceof IData) {
            return (IData)value;
        } else {
            return null;
        }
    }

    /**
     * get data
     * @param name
     * @param def
     * @return
     */
    public IData getData(String name, IData def) {
        Object value = super.get(name);
        if (value == null)
            return def;

        if (value instanceof IData) {
            return (IData)value;
        } else {
            return def;
        }
    }

    /**
     * get dataset
     * @param name Key
     * @param def 默认值
     * @return
     */
    @SuppressWarnings("unchecked")
    public IDataset getDataset(String name, IDataset def) {
        Object value = super.get(name);
        if (value == null)
            return def;

        if (value instanceof IDataset) {
            return (IDataset)value;
        } if (value instanceof JSONArray) {
            IDataset ds = new DatasetList();
            ds.addAll((JSONArray) value);
            return ds;
        } else {
            return def;
        }
    }

    /**
     * get data
     * @param name
     * @return IDataset
     */
    public IDataset getDataset(String name) {
        return getDataset(name, null);
    }

    /**
     * sub data
     * @param group
     * @return IData
     * @throws Exception
     */
    public IData subData(String group) throws Exception {
        return subData(group, false);
    }

    /**
     * sub data
     * @param group
     * @param istrim
     * @return IData
     * @throws Exception
     */
    public IData subData(String group, boolean istrim) throws Exception {
        IData element = new DataMap();

        String[] names = getNames();
        String prefix = group + "_";
        for (String name : names) {
            if (name.startsWith(prefix)) {
                element.put(istrim ? name.substring((prefix).length()) : name, get(name));
            }
        }

        return element;
    }

    public String put(String key, String value){
        return (String)super.put(key, value);
    }

    public IData put(String key, IData value){
        return (IData)super.put(key, value);
    }

    public IDataset put(String key, IDataset value){
        return (IDataset)super.put(key, value);
    }

    /**
     * to string
     * @return String
     */
    public String toString() {
        return JSONObject.toJSONString(this).toString();
    }

    /**
     * to XML string
     * @return XMLString
     */
    public String toXMLString(){
        StringBuilder strBuilder = new StringBuilder();
        for (String key : this.keySet())
        {
            if (key == null)
            {
                continue;
            }
            strBuilder.append("<").append(key.toString()).append(">");
            Object value = this.get(key);
            if (value != null) {
                if(value instanceof String){
                    strBuilder.append((String)value);
                }else if (value instanceof IData) {
                    strBuilder.append(((IData)value).toXMLString());
                }else if(value instanceof IDataset){
                    strBuilder.append(((IDataset)value).toXMLString());
                }else{
                    strBuilder.append(value);
                }

            }
            strBuilder.append("</").append(key.toString()).append(">");
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
