package com.edu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.edu.dao.IF_BoardDAO;
import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 프로젝트에서 공통으로 사용하는  유틸리티기능을 모아놓은 클래스.
 * @author 전민아
 *컨트롤러 기능 @Controller
 *콤포턴트@Component는 MVC가 아닌 기능들을 모아놓은 스프링빈 메서드
 */
@Controller
public class CommonUtil {
	//멤버변수생성(아래)
	private Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	@Inject
	private IF_MemberService memberService;//스프링빈을 주입받아서(DI)객체준비
	@Inject
	private IF_BoardDAO boardDAO;
	

	//첨부파일 업로드/다운로드/삭제/인서트/수정에 모두 사용될 저장경로 1개 지정해서 전역으로 사용
	@Resource(name="uploadPath")
	private String uploadPath;			
	public String getUploadPath() {
		return uploadPath;
	}
	
	//첨부파일 개별 삭제 ajax로 받아서 처리
	@RequestMapping(value="/file_delete", method=RequestMethod.POST)
	@ResponseBody
	public String file_delete(@RequestParam("save_file_name")String save_file_name) {
		String result = "";
		try {
			boardDAO.deleteAttach(save_file_name);
			File target = new File(uploadPath +"/" + save_file_name);
			if(target.exists()) {
				target.delete();
			}
			result = "success";
		} catch (Exception e) {
			result = "fail: " + e.toString();
		}		
		return result;
	}	
	//다운로드 처리도 같은 페이지에서 결과 값 반환받는 @ResponseBody 사용
	@RequestMapping(value="/download", method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource download (@RequestParam("save_file_name")String save_file_name, @RequestParam("real_file_name")String real_file_name, HttpServletResponse response) throws Exception {
		//FileSystem 은 스츠링 코어에서 제공하는 전용 파일처리 클래스
		File file = new File(uploadPath +"/" + save_file_name);
		response.setContentType("application/download; utf-8");
		real_file_name = URLEncoder.encode(real_file_name);
		response.setHeader("Content-Disposition", "attachment; filename=" + real_file_name);
		return new FileSystemResource(file);
	}
	
	//페이지이동이 아닌 같은 페이지에 결과값만 반환하는 @ResponseBody
	@RequestMapping(value="/image_preview", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> imagePreview(@RequestParam("save_file_name")String save_file_name, HttpServletResponse response) throws Exception {
		//파일을 입출력할때는 파일을byte형식으로 입출력할때 발생되는 통로 스트림이 발생
		FileInputStream fis = null;//입력통로
		ByteArrayOutputStream baos = new ByteArrayOutputStream();//출력 통로
		fis = new FileInputStream(uploadPath + "/" + save_file_name);
		int readCount = 0;
		byte[] buffer = new byte[1024]; //buffer임시저장소 크기 지정
		byte[] fileArray = null;//출력스트림결과 저장하는 공간
		//반복문:목적, fis입력받는 save_file_name byte값이 (배열) -1일때 까지 반복
		while((readCount=fis.read(buffer)) != -1) {
			//입력통로fis에서 출력통로로 baos보냅니다
			baos.write(buffer, 0, readCount);
			//결과는 baos에 누적된 결과가 들어갑니다. jsp로 보내주면됩니다.
		}
		fileArray = baos.toByteArray();
		fis.close();//메모리 초기화
		baos.close();
		//fileArray값을 jsp로 보내주는 준비작업 final이 메서드에서만 사용하겠다고 명시
		final HttpHeaders headers = new HttpHeaders();
		String ext = FilenameUtils.getExtension(save_file_name);
		//이미지 확장자에 따라서 매칭되는 헤더 값이 변해야 이미지 미리보기가 정상으로 보임
		switch(ext.toLowerCase()) {
		case "png":
			headers.setContentType(MediaType.IMAGE_PNG);
			break;
		case "jpg":
			headers.setContentType(MediaType.IMAGE_JPEG);
			break;
		case "gif":
			headers.setContentType(MediaType.IMAGE_GIF);
			break;
		case "jpeg":
			headers.setContentType(MediaType.IMAGE_JPEG);
			break;
		case "bmp":
			headers.setContentType(MediaType.parseMediaType("image/bmp"));
			break;
		default:break;
		}
		
		return new ResponseEntity<byte[]>
		(fileArray,headers,HttpStatus.CREATED);
	} 
	//XSS크로스사이트스크립트 방지용 코드를 파싱하는 메서드(아래)
	public String unScript(String data) {
		//if(data == null || data.trim().equals("")) {
		if(data.isEmpty()) {
			return"";
		}
		String ret = data;
		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");
        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");
        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");
        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");
		return ret;
	}
	
	
	//첨부파일이 이미지인지 아닌지 확인하는 데이터  생성
	private ArrayList<String> checkImgArray = new ArrayList<String>() {
		{
		add("gif");
		add("jpg");
		add("jpeg");
		add("png");
		add("bmp");
		}
	};
	public ArrayList<String> getCheckImgArray() {
		return checkImgArray;
	}
	//RestAPI서버 맛보기ID중복체크(제대로 만들면 @RestController 사용)
	@RequestMapping(value="/id_check", method=RequestMethod.GET)
	@ResponseBody//반환받는 값의 헤더 값을 제외하고, 내용(body)만 반환
	public String id_check(@RequestParam("user_id")String user_id) throws Exception {
		//중복아이디를 체크로지(아래)
		String memberCnt = "1";//중복ID가 있을때, 기본값1
		if(!user_id.isEmpty()) {
		MemberVO memberVO = memberService.readMember(user_id);
		logger.info("디버그: "+ memberVO);//user_id를 공백을 전송해도 null이기때문에 조건 추가 필요
		if(memberVO == null) {//중복아이디가 존재하면{}안을 실행
			memberCnt = "0";
			}	
		}
		return memberCnt;
	}
	//파일업로드 공통 메서드
	public String fileUpload(MultipartFile file) throws IOException {
		// TODO UUID클래스로 저장될 고유식별 파일명을 생성 후 물리적으로 저장
		String realFileName = file.getOriginalFilename();
		//폴더에 저장할 PK파일명을 생성
		UUID uid = UUID.randomUUID();
		String saveFileName = uid.toString() + "." + StringUtils.getFilenameExtension(realFileName);
		byte[] fileData = file.getBytes();
		File target = new File(uploadPath, saveFileName);
		FileCopyUtils.copy(fileData, target);
		return saveFileName;
	}

	public void profile_upload(String user_id, HttpServletRequest request, MultipartFile file) throws IOException {
		// TODO 프로필이미지는 보안이 필요한 폴더가 아닌, resources폴더에 업로드 처리. 서버의 경로필요
		String folderPath = request.getServletContext().getRealPath("/resources/profile");
		File makeFolder = new File(folderPath);
		if(!makeFolder.exists()) {
			makeFolder.mkdir();
		}
		byte[] fileData = file.getBytes();
		File target =  new File(makeFolder,user_id+".png");
		FileCopyUtils.copy(fileData, target);
	}

	public void profile_delete(String user_id, HttpServletRequest request) {
		// 프로필 이미지가 프로필 폴더에 존재하면 삭제하는 로직
		String folderPath = request.getServletContext().getRealPath("/resources/profile");
		File target = new File("","");
		if(target.exists()) {
			System.out.println("debug119 " + target);
			target.delete();
		}
		
	}
}