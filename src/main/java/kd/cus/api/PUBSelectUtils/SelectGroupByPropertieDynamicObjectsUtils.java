package kd.cus.api.PUBSelectUtils;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.cus.api.PUBEntitys.FilterEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 获取数组中的相同属性却不同值的sql语句进行融合的工具类
 * 例如查询某个表的id属性的值为 1,2,3的值的sql
 * 会将其简化为select * from table where id in (1,2,3)
 * 主要用于保存单据的场景
 *
 */

public class SelectGroupByPropertieDynamicObjectsUtils {

    private static String fetchGroupKey(FilterEntity filterEntity){
        return filterEntity.getEntityNumber() + "@_@" + filterEntity.getPropertie()  + "@_@" + filterEntity.getOrg()+ "@_@" + filterEntity.getGetPropertie();
    }

    /**
     *获取的属性与查询时的属性一致，只有这个查询时的属性以及id才会有数值，其余属性为空
     * 用户简单的获取DynamicObject的对象
     * @return
     */
    public static Map<String, DynamicObject> loadAll(Map<String, DynamicObject> list, List<FilterEntity> filterList){
        if (null == list){
            list = new HashMap<>();
        }
        Map<String,List<FilterEntity>> cateMap = filterList
                .stream()
                .collect(Collectors.groupingBy(e ->fetchGroupKey(e)));
        Iterator<Map.Entry<String, List<FilterEntity>>> cateMapIt = cateMap.entrySet().iterator();
        while (cateMapIt.hasNext()){
            Map.Entry<String, List<FilterEntity>> item = cateMapIt.next();
            List<String> values = new ArrayList<>();
            item.getValue().forEach(filterEntity -> {
                values.add(filterEntity.getValue());
            });
            String entityNumber = item.getKey().split("@_@")[0];
            String propertie = item.getKey().split("@_@")[1];
            String org = item.getKey().split("@_@")[2];
            QFilter qFilter = new QFilter(propertie,QCP.in, values);
            if (!"null".equals(org) && StringUtils.isNotBlank(org)){
                qFilter.and(new QFilter("org",QCP.equals,org));
            }
            DynamicObject[] dys = BusinessDataServiceHelper.load(entityNumber, propertie, new QFilter[]{qFilter});
            for(DynamicObject dy : dys){
                list.put(entityNumber+ "@_@" +propertie + "@_@" + dy.getString(propertie),dy);
            }
        }
        return list;
    }

    /**
     * 楼上的进化版
     * 能够自主的获取特定的属性值
     * @param list
     * @param filterList
     * @return
     */
    public static Map<String, DynamicObject> loadAllPRO(Map<String, DynamicObject> list, List<FilterEntity> filterList){
        if (null == list){
            list = new HashMap<>();
        }
        Map<String,List<FilterEntity>> cateMap = filterList
                .stream()
                .collect(Collectors.groupingBy(e ->fetchGroupKey(e)));
        Iterator<Map.Entry<String, List<FilterEntity>>> cateMapIt = cateMap.entrySet().iterator();
        while (cateMapIt.hasNext()){
            Map.Entry<String, List<FilterEntity>> item = cateMapIt.next();
            List<String> values = new ArrayList<>();
            item.getValue().forEach(filterEntity -> {
                values.add(filterEntity.getValue());
            });
            String entityNumber = item.getKey().split("@_@")[0];
            String propertie = item.getKey().split("@_@")[1];
            String getPropertie = item.getKey().split("@_@")[3];
            String org = item.getKey().split("@_@")[2];
            QFilter qFilter = new QFilter(propertie,QCP.in, values);
            if (!"null".equals(org) && StringUtils.isNotBlank(org)){
                qFilter.and(new QFilter("org",QCP.equals,org));
            }
            DynamicObject[] dys = BusinessDataServiceHelper.load(entityNumber, getPropertie, new QFilter[]{qFilter});
            for(DynamicObject dy : dys){
                list.put(entityNumber+ "@_@" +propertie + "@_@" + dy.getString(propertie),dy);
            }
        }
        return list;
    }
}
