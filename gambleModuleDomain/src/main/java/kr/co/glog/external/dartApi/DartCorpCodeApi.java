package kr.co.glog.external.dartApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.stock.dao.DartCompanyDao;
import kr.co.glog.domain.stock.entity.DartCompany;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *  DART 고유번호 다운로드
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartCorpCodeApi {

    private final DartCompanyDao DartCompanyDao;

    // 기본 URL
    public static String url = "https://opendart.fss.or.kr/api/corpCode.xml?crtfc_key=4324bafbe5c2c9436bc1bdc4ccb907adc3c41110";

    /**
     * DART API로 코드 파일 받아서 DB 업데이트
     */
    public void updateCorpCode() {
        File file = extract( download() );
        parse( file );
    }

    /**
     * 다운로드
     * @throws ApplicationRuntimeException
     */
    public File download() throws ApplicationRuntimeException {

        File zipFile = null;

        try {
            zipFile = new File("corpCode.zip");
            FileUtils.copyURLToFile(new URL(url), zipFile);
        } catch (Exception e) {
            throw new ApplicationRuntimeException("DART 고유번호 파일 다운로드 중 오류가 발생했습니다.");
        }

        return zipFile;
    }

    /**
     * 압축해제
     * @return
     * @throws ApplicationRuntimeException
     */
    public File extract( File zipFile ) throws ApplicationRuntimeException {
        try {
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if ( zipEntry == null || zipEntry.isDirectory() ) {
                throw new ApplicationRuntimeException("DART 고유번호 압축파일에 문제가 있습니다.");
            }

            String filename = zipEntry.getName();
            File file = new File(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int length;
            byte[] buffer = new byte[1024];
            while (( length = zipInputStream.read(buffer) ) > 0) {
                fileOutputStream.write( buffer, 0, length );
            }
            fileOutputStream.close();

            return file;

        } catch ( Exception e ) {
            throw new ApplicationRuntimeException("DART 고유번호 압축 해제 중 오류가 발생했습니다.");
        }
    }

    /**
     * corpCode.xml 읽어서 리턴
     * @param file
     * @return
     */
    public void parse( File file ) {

        ArrayList<DartCompany> DartCompanyList = new ArrayList<DartCompany>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse( file );
            log.debug( document.toString() );
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("list");
            int stockCodeSize = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if ( node.getNodeType() == Node.ELEMENT_NODE ) {

                    Element element = (Element) node;
                    DartCompany DartCompany = new DartCompany();

                    Node childNode = element.getElementsByTagName("corp_code").item(0);
                    DartCompany.setCorpCode( childNode.getChildNodes().item(0).getNodeValue() );

                    childNode = element.getElementsByTagName("corp_name").item(0);
                    DartCompany.setCompanyName( childNode.getChildNodes().item(0).getNodeValue() );

                    childNode = element.getElementsByTagName("stock_code").item(0);
                    DartCompany.setStockCode( childNode.getChildNodes().item(0).getNodeValue().trim() );

                    childNode = element.getElementsByTagName("modify_date").item(0);
                    DartCompany.setModifyDate( childNode.getChildNodes().item(0).getNodeValue() );

                    DartCompanyList.add( DartCompany );
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException("DART 고유번호 XML 파일을 읽는 중 오류가 발생했습니다.");
        }

        for ( DartCompany DartCompany : DartCompanyList ) {
            /*
            try {
                log.debug(DartCompany.toString());
                DartCompanyDao.insertDartCompany( DartCompany );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                DartCompanyDao.updateDartCompany( DartCompany );
            }
            */

            try {
                log.debug(DartCompany.toString());
                if ( DartCompanyDao.updateDartCompany( DartCompany ) == 0 ) {
                    DartCompanyDao.insertDartCompany( DartCompany );
                };
            } catch ( Exception e ) {

            }
        }

    }





}
