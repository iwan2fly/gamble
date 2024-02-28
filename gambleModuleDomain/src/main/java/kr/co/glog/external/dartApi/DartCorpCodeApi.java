package kr.co.glog.external.dartApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.entity.Company;
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

    private final CompanyDao companyDao;

    // 기본 URL
    public static String url = "https://opendart.fss.or.kr/api/corpCode.xml?crtfc_key=" + DartKey.DART_CRTFC_KRY;

    /**
     * DART API로 코드 파일 받아서 DB 업데이트
     */
    public void updateCorpCode() {
        // 파일 다운로드 받아서 압축 해제
        File file = extract( download() );

        // 파싱 후 회사 목록 리턴
        ArrayList<Company> companyList = parse( file );

        // 회사 목록을 DB에 upsert
        upsertCompany( companyList );
    }

    /**
     * 다운로드
     * @throws ApplicationRuntimeException
     */
    private File download() throws ApplicationRuntimeException {

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
    private File extract( File zipFile ) throws ApplicationRuntimeException {
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
     * corpCode.xml 읽어서 인서트
     * @param file
     * @return
     */
    private ArrayList<Company> parse( File file ) {

        ArrayList<Company> companyList = new ArrayList<Company>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            log.debug(document.toString());
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("list");
            int stockCodeSize = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    Company Company = new Company();

                    Node childNode = element.getElementsByTagName("corp_code").item(0);
                    Company.setCompanyCode(childNode.getChildNodes().item(0).getNodeValue());

                    childNode = element.getElementsByTagName("corp_name").item(0);
                    Company.setCompanyName(childNode.getChildNodes().item(0).getNodeValue());

                    childNode = element.getElementsByTagName("stock_code").item(0);
                    Company.setStockCode(childNode.getChildNodes().item(0).getNodeValue().trim());

                    childNode = element.getElementsByTagName("modify_date").item(0);
                    Company.setModifyDate(childNode.getChildNodes().item(0).getNodeValue());

                    companyList.add(Company);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException("DART 고유번호 XML 파일을 읽는 중 오류가 발생했습니다.");
        }

        return companyList;
    }

    private void upsertCompany( ArrayList<Company> companyList ) {
        for ( Company Company : companyList) {

            try {
                log.debug(Company.toString());
                if ( companyDao.updateCompany(Company) == 0 ) {
                    companyDao.insertCompany(Company);
                };
            } catch ( Exception e ) {

            }
        }

    }





}
