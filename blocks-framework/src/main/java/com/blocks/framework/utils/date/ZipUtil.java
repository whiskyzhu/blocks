/**
 * @Title: ZipUtil.java.
 * @Package com.ztesoft.uboss.coomon.utils
 * Copyright: Copyright (c) 2015年5月18日
 * Company:南京中兴软创科技股份有限公司
 * Organization: 智慧交通BU
 * @author WangWei
 * @date 2015年5月18日 下午5:54:36
 * @version V1.0
 */
package com.blocks.framework.utils.date;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p>Class Name: ZipUtil.</p>
 * <p>Description: 类功能说明</p>
 * <p>Sample: 该类的典型使用方法和用例</p>
 * <p>Author: WangWei</p>
 * <p>Date: 2015年5月18日</p>
 * <p>Modified History: 修改记录，格式(Name)  (Version)  (Date) (Reason & Contents)</p>
 */
public class ZipUtil {
    public static final int BUFFER = 1024;

    /** 
     * 数据压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */
    public static void compress(InputStream is, OutputStream os) throws Exception {

        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    /** 
     * 数据解压缩 
     *  
     * @param is 
     * @param os 
     * @throws Exception 
     */
    public static void decompress(InputStream is, OutputStream os) throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    /** 
     * 数据压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 压缩  
        compress(bais, baos);

        byte[] output = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return output;
    }

    /** 
     * 数据解压缩 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 解压缩  

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    // 压缩   
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    // 解压缩   
    public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[BUFFER];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString("UTF-8");
    }

    public static void main(String args[]) {
        String pre = new String(
                "中文http://111./../2/:990  106750 2015-05-14 14:34:09.662 [Thread-9] ERROR netMan- ,errorCode=[S-DAT-00010],errorCodeEnd,errorSource=null],type : 1,errorSourceEnd,errorDesc=error accurs getting connection!,errorDescEnd,causeByNms=java.net.SocketException: Connection reset,causeByNmsEnd                                                                                                                                             "
                        + "106762 2015-05-14 14:34:09.674 [Thread-9] ERROR netMan- com.ztesoft.uboss.core.exception.BaseAppException: [S-DAT-00010] Io exception: Connection reset,com.ztesoft.uboss.core.exception.ExceptionHandler.publish(ExceptionHandler.java:277) ~[uboss-core-2.0.1-SNAPSHOT.jar:na],com.ztesoft.uboss.core.exception.ExceptionHandler.publish(ExceptionHandler.java:155) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]       "
                        + ",com.ztesoft.uboss.core.jdbc.ses.ThreadLocalSession.getConnection(ThreadLocalSession.java:434) ~[uboss-core-2.0.1-SNAPSHOT.jar:na],com.ztesoft.uboss.core.jdbc.JdbcTemplate.query(JdbcTemplate.java:242) ~[uboss-core-2.0.1-SNAPSHOT.jar:na],com.ztesoft.uboss.core.jdbc.BaseDAO.query(BaseDAO.java:219) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                    "
                        + ",com.ztesoft.uboss.core.service.dao.oracleimpl.ServiceConfigDAOOracle.queryServiceByName(ServiceConfigDAOOracle.java:262) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                   "
                        + ",com.ztesoft.uboss.core.service.ActionConfig.getServiceDto0(ActionConfig.java:157) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                          "
                        + ",com.ztesoft.uboss.core.service.ActionConfig.getServiceDto(ActionConfig.java:123) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                           "
                        + ",com.ztesoft.uboss.core.service.ActionConfig.getCachedService(ActionConfig.java:86) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                         "
                        + ",com.ztesoft.uboss.core.service.ActionConfig.getService(ActionConfig.java:103) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                              "
                        + ",com.ztesoft.uboss.core.service.ServiceFlow.getServiceDto(ServiceFlow.java:223) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                             "
                        + ",com.ztesoft.uboss.core.service.ServiceFlow.callService(ServiceFlow.java:104) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                               "
                        + ",com.ztesoft.uboss.sts.core.mq.MQHandler.createMqSource(MQHandler.java:150) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                                 "
                        + ",com.ztesoft.uboss.sts.core.mq.MQHandler.<clinit>(MQHandler.java:52) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                                        "
                        + ",com.ztesoft.uboss.sts.dpe.datafusion.spouts.mq.consumer.VehicleEPoliceMQConsumer.run(VehicleEPoliceMQConsumer.java:70) [dpe-cep-datafusion-topology-spouts-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                              "
                        + ",java.lang.Thread.run(Thread.java:662) [na:1.6.0_27]                                                                                                                                                                                                                                                                                                                                                            "
                        + "Caused by: java.sql.SQLException: Io exception: Connection reset                                                                                                                                                                                                                                                                                                                                                "
                        + ",oracle.jdbc.driver.SQLStateMapping.newSQLException(SQLStateMapping.java:74) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                             "
                        + ",oracle.jdbc.driver.DatabaseError.newSQLException(DatabaseError.java:131) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                "
                        + ",oracle.jdbc.driver.DatabaseError.throwSqlException(DatabaseError.java:197) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                              "
                        + ",oracle.jdbc.driver.DatabaseError.throwSqlException(DatabaseError.java:261) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                              "
                        + ",oracle.jdbc.driver.DatabaseError.throwSqlException(DatabaseError.java:566) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                              "
                        + ",oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:418) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                          "
                        + ",oracle.jdbc.driver.PhysicalConnection.<init>(PhysicalConnection.java:508) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                               "
                        + ",oracle.jdbc.driver.T4CConnection.<init>(T4CConnection.java:203) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                         "
                        + ",oracle.jdbc.driver.T4CDriverExtension.getConnection(T4CDriverExtension.java:33) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                         "
                        + ",oracle.jdbc.driver.OracleDriver.connect(OracleDriver.java:510) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                          "
                        + ",com.ztesoft.uboss.core.jdbc.mypool.MyPool.newPhysicalConnection(MyPool.java:255) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                           "
                        + ",com.ztesoft.uboss.core.jdbc.mypool.MyPool.newConnection(MyPool.java:265) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                                   "
                        + ",com.ztesoft.uboss.core.jdbc.mypool.MyPool.open(MyPool.java:154) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                                            "
                        + ",com.ztesoft.uboss.core.jdbc.mypool.MyPool.open(MyPool.java:108) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                                                            "
                        + ",com.ztesoft.uboss.core.jdbc.ds.MyPoolConnectionProvider.createMyPool(MyPoolConnectionProvider.java:152) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                    "
                        + ",com.ztesoft.uboss.core.jdbc.ds.MyPoolConnectionProvider.configure(MyPoolConnectionProvider.java:45) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                        "
                        + ",com.ztesoft.uboss.core.jdbc.ds.ConnectionProviderFactory.configure(ConnectionProviderFactory.java:148) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                     "
                        + ",com.ztesoft.uboss.core.jdbc.ds.ConnectionProviderFactory.configure(ConnectionProviderFactory.java:143) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                     "
                        + ",com.ztesoft.uboss.core.jdbc.ds.ConnectionProviderFactory.getConnectionProvider(ConnectionProviderFactory.java:60) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                          "
                        + ",com.ztesoft.uboss.core.jdbc.ds.ConnectionProviderFactory.getConnection(ConnectionProviderFactory.java:177) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                 "
                        + ",com.ztesoft.uboss.core.jdbc.ses.ThreadLocalSession.getConnection(ThreadLocalSession.java:428) ~[uboss-core-2.0.1-SNAPSHOT.jar:na]                                                                                                                                                                                                                                                                              "
                        + "  ... 13 common frames omitted                                                                                                                                                                                                                                                                                                                                                                                  "
                        + "Caused by: java.net.SocketException: Connection reset                                                                                                                                                                                                                                                                                                                                                           "
                        + ",java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:96) ~[na:1.6.0_27]                                                                                                                                                                                                                                                                                                                             "
                        + ",java.net.SocketOutputStream.write(SocketOutputStream.java:136) ~[na:1.6.0_27]                                                                                                                                                                                                                                                                                                                                  "
                        + ",oracle.net.ns.DataPacket.send(DataPacket.java:150) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                                      "
                        + ",oracle.net.ns.NetOutputStream.flush(NetOutputStream.java:180) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                           "
                        + ",oracle.net.ns.NetInputStream.getNextPacket(NetInputStream.java:169) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                     "
                        + ",oracle.net.ns.NetInputStream.read(NetInputStream.java:117) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                              "
                        + ",oracle.net.ns.NetInputStream.read(NetInputStream.java:92) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                               "
                        + ",oracle.net.ns.NetInputStream.read(NetInputStream.java:77) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                               "
                        + ",oracle.jdbc.driver.T4CMAREngine.unmarshalUB1(T4CMAREngine.java:1034) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                    "
                        + ",oracle.jdbc.driver.T4CMAREngine.unmarshalSB1(T4CMAREngine.java:1010) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                    "
                        + ",oracle.jdbc.driver.T4CTTIoauthenticate.receiveOauth(T4CTTIoauthenticate.java:760) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                       "
                        + ",oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:368) ~[ojdbc5-1.0.jar:Oracle JDBC Driver version - ,11.1.0.7.0-Production,]                                                                                                                                                                                                                                                                          "
                        + "  ... 28 common frames omitted                                                                                                                                                                                                                                                                                                                                                                                  "
                        + "106763 2015-05-14 14:34:09.675 [Thread-9] ERROR netMan- ,,                                                                                                                                                                                                                                                                                                                                                      "
                        + "<?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_VaCa</ServiceName><Data><cycleRequest>1000</cycleRequest><TIME>1429148300000</TIME><GIS_SEGMENT_GEO_ID_LIST></GIS_SEGMENT_GEO_ID_LIST><LONGTIME>1429148300000</LONGTIME><REQUEST>true</REQUEST><BZ>ServicesRequest_ServiceProxy_VaCaqryCell2XYByLaneIDs</BZ><IS_TEST>true</IS_TEST><method>qryCell2XYByLaneIDs</method><ROTAS></ROTAS><METHOD>qryCell2XYByLaneIDs</METHOD><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_DeviceInfo</ServiceName><Data><guTime>1408517788000</guTime><cycleRequest>1000</cycleRequest><DEVICEIDLIST>[Ljava.lang.Object;@33df13b8</DEVICEIDLIST><method>qryDeviceStateInfo</method><REQUEST>true</REQUEST><METHOD>qryDeviceStateInfo</METHOD><SERVICE_NAME>ServiceProxy_DeviceInfo</SERVICE_NAME><isRequestBus>true</isRequestBus><BZ>ServicesRequest_ServiceProxy_DeviceInfoqryDeviceStateInfo</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><SERVICE_NAME>ServiceProxy_VaCa</SERVICE_NAME><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_UTCStatus</ServiceName><Data><cycleRequest>1000</cycleRequest><OBJECTIDLIST>29</OBJECTIDLIST><OBJECTIDLIST>28</OBJECTIDLIST><OBJECTIDLIST>26</OBJECTIDLIST><OBJECTIDLIST>40</OBJECTIDLIST><OBJECTIDLIST>34</OBJECTIDLIST><OBJECTIDLIST>33</OBJECTIDLIST><OBJECTIDLIST>27</OBJECTIDLIST><OBJECTIDLIST>44</OBJECTIDLIST><OBJECTIDLIST>32</OBJECTIDLIST><LONGTIME>1428910191000</LONGTIME><method>qryUtcStatusByIdTime</method><IS_TEST>true</IS_TEST><REQUEST>true</REQUEST><METHOD>qryUtcStatusByIdTime</METHOD><UtcStatus_LIST><CROSSID>29</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>28</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>26</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>40</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>34</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>33</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>27</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>44</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>32</CROSSID></UtcStatus_LIST><TIMESTAMP>1428910191000</TIMESTAMP><SERVICE_NAME>ServiceProxy_UTCStatus</SERVICE_NAME><BZ>ServicesRequest_ServiceProxy_UTCStatusqryUtcStatusByIdTime</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_UTCStatus</ServiceName><Data><cycleRequest>1000</cycleRequest><OBJECTIDLIST>29</OBJECTIDLIST><OBJECTIDLIST>28</OBJECTIDLIST><OBJECTIDLIST>26</OBJECTIDLIST><OBJECTIDLIST>40</OBJECTIDLIST><OBJECTIDLIST>34</OBJECTIDLIST><OBJECTIDLIST>33</OBJECTIDLIST><OBJECTIDLIST>27</OBJECTIDLIST><OBJECTIDLIST>44</OBJECTIDLIST><OBJECTIDLIST>32</OBJECTIDLIST><LONGTIME>1428910191000</LONGTIME><method>qryUtcStatusByIdTime</method><IS_TEST>true</IS_TEST><REQUEST>true</REQUEST><METHOD>qryUtcStatusByIdTime</METHOD><UtcStatus_LIST><CROSSID>29</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>28</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>26</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>40</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>34</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>33</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>27</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>44</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>32</CROSSID></UtcStatus_LIST><TIMESTAMP>1428910191000</TIMESTAMP><SERVICE_NAME>ServiceProxy_UTCStatus</SERVICE_NAME><BZ>ServicesRequest_ServiceProxy_UTCStatusqryUtcStatusByIdTime</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss>");

        pre = new String("<?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_VaCa</ServiceName><Data><cycleRequest>1000</cycleRequest><TIME>1429148300000</TIME><GIS_SEGMENT_GEO_ID_LIST></GIS_SEGMENT_GEO_ID_LIST><LONGTIME>1429148300000</LONGTIME><REQUEST>true</REQUEST><BZ>ServicesRequest_ServiceProxy_VaCaqryCell2XYByLaneIDs</BZ><IS_TEST>true</IS_TEST><method>qryCell2XYByLaneIDs</method><ROTAS></ROTAS><METHOD>qryCell2XYByLaneIDs</METHOD><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_DeviceInfo</ServiceName><Data><guTime>1408517788000</guTime><cycleRequest>1000</cycleRequest><DEVICEIDLIST>[Ljava.lang.Object;@33df13b8</DEVICEIDLIST><method>qryDeviceStateInfo</method><REQUEST>true</REQUEST><METHOD>qryDeviceStateInfo</METHOD><SERVICE_NAME>ServiceProxy_DeviceInfo</SERVICE_NAME><isRequestBus>true</isRequestBus><BZ>ServicesRequest_ServiceProxy_DeviceInfoqryDeviceStateInfo</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><SERVICE_NAME>ServiceProxy_VaCa</SERVICE_NAME><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_UTCStatus</ServiceName><Data><cycleRequest>1000</cycleRequest><OBJECTIDLIST>29</OBJECTIDLIST><OBJECTIDLIST>28</OBJECTIDLIST><OBJECTIDLIST>26</OBJECTIDLIST><OBJECTIDLIST>40</OBJECTIDLIST><OBJECTIDLIST>34</OBJECTIDLIST><OBJECTIDLIST>33</OBJECTIDLIST><OBJECTIDLIST>27</OBJECTIDLIST><OBJECTIDLIST>44</OBJECTIDLIST><OBJECTIDLIST>32</OBJECTIDLIST><LONGTIME>1428910191000</LONGTIME><method>qryUtcStatusByIdTime</method><IS_TEST>true</IS_TEST><REQUEST>true</REQUEST><METHOD>qryUtcStatusByIdTime</METHOD><UtcStatus_LIST><CROSSID>29</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>28</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>26</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>40</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>34</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>33</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>27</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>44</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>32</CROSSID></UtcStatus_LIST><TIMESTAMP>1428910191000</TIMESTAMP><SERVICE_NAME>ServiceProxy_UTCStatus</SERVICE_NAME><BZ>ServicesRequest_ServiceProxy_UTCStatusqryUtcStatusByIdTime</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_UTCStatus</ServiceName><Data><cycleRequest>1000</cycleRequest><OBJECTIDLIST>29</OBJECTIDLIST><OBJECTIDLIST>28</OBJECTIDLIST><OBJECTIDLIST>26</OBJECTIDLIST><OBJECTIDLIST>40</OBJECTIDLIST><OBJECTIDLIST>34</OBJECTIDLIST><OBJECTIDLIST>33</OBJECTIDLIST><OBJECTIDLIST>27</OBJECTIDLIST><OBJECTIDLIST>44</OBJECTIDLIST><OBJECTIDLIST>32</OBJECTIDLIST><LONGTIME>1428910191000</LONGTIME><method>qryUtcStatusByIdTime</method><IS_TEST>true</IS_TEST><REQUEST>true</REQUEST><METHOD>qryUtcStatusByIdTime</METHOD><UtcStatus_LIST><CROSSID>29</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>28</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>26</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>40</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>34</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>33</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>27</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>44</CROSSID></UtcStatus_LIST><UtcStatus_LIST><CROSSID>32</CROSSID></UtcStatus_LIST><TIMESTAMP>1428910191000</TIMESTAMP><SERVICE_NAME>ServiceProxy_UTCStatus</SERVICE_NAME><BZ>ServicesRequest_ServiceProxy_UTCStatusqryUtcStatusByIdTime</BZ><IS_LOGGED_1220>true</IS_LOGGED_1220></Data></uboss>");
        pre = new String("中文http://111./../2/:990HOD>qryCell2XYByLaneIDs</METHOD><?xml version='1.0' encoding='UTF-8'?><uboss><ServiceName>ServiceProxy_Devic");

        try {
            System.out.println("Pre Length:" + pre.length());
            String out = ZipUtil.compress(pre);
            System.out.println("Out Length:" + out.length());

            String out2 = ZipUtil.decompress(ZipUtil.compress(pre));

            System.out.println("Decompress Length:" + out2.length());
            

            System.out.println(out2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
