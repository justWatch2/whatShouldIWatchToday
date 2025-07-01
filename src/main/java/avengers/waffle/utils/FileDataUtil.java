package avengers.waffle.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Controller
@RequiredArgsConstructor
@Slf4j
public class FileDataUtil {
	
	private ArrayList<String> extNameArray = new ArrayList<String>() // 허용하는 확장자 정의를 한 것.
	{
		{
			add("gif");
			add("jpg");
			add("png");
		}
	};     //<-- 현재 코드는 활용하지는 않는다.. 얘는 선언이지 기능이 동작하지는 않는다. 절대 미리 예측 금지..
	
	//첨부파일 업로드 경로 변수값으로 가져옴 servlet-context.xml
	private String uploadPath="C:/Users/Admin/Desktop/projectimg";

    public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 게시물 상세보기에서 첨부파일 다운로드 메서드 구현(공통)
	 */
	@RequestMapping(value="/img/download", method=RequestMethod.GET)
	@ResponseBody   // 어떤 데이터를 포함하여 전송.. 어노테이션.. view지정하지 않고 바로 클라이언트 요청으로 응답.
	public FileSystemResource fileDownload(@RequestParam("filename") String fileName, HttpServletResponse response) {
		System.out.println("arrive?");
		File file = new File(uploadPath + "/" + fileName);
		System.out.println(file.getName());
		System.out.println(file.getAbsolutePath());
		response.setContentType("application/download; utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		return new FileSystemResource(file);
	}
	
	/**
	 * 파일 업로드 메서드(공통)
	 * @throws IOException 
	 */
	public String[] fileUpload(MultipartFile[] file) throws IOException {
		 String path = System.getProperty("user.dir");
	        System.out.println("현재 작업 경로: " + path);
			if(file==null)
				return new String[0];

		// 1. 업로드 경로를 File 객체로 만듭니다.
		File uploadDir = new File(uploadPath);

		// 2. 만약 폴더가 존재하지 않으면, 폴더를 생성합니다.
		//    mkdirs()는 부모 폴더가 없어도 알아서 다 만들어줍니다.
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		String[] files = new String[file.length];
		for(int i=0; i < file.length; i++) {
			if(file[i].getOriginalFilename()!="") {  // 실제 file객체가 존재한다면
				String originalName = file[i].getOriginalFilename();//확장자가져오기 위해서 전체파일명을 가져옴.
				UUID uid = UUID.randomUUID();//랜덤문자 구하기 맘에안든다. 
				String saveName = uid.toString() + "." + originalName.split("\\.")[1];//한글 파일명 처리 때문에...
				// 
//			String[] files = new String[] {saveName}; //형변환  files[0] 파일명이 들어 간다..
				byte[] fileData = file[i].getBytes();

				File target = new File(uploadPath, saveName);
				FileCopyUtils.copy(fileData, target);
				files[i]="/img/download?filename="+saveName;
			}			
		}		
		return files;
	}

    public void setExtNameArray(ArrayList<String> extNameArray) {
		this.extNameArray = extNameArray;
	}

	public void deleteFile(String fileUrl) {
		// fileUrl이 유효하지 않으면 아무것도 하지 않음
		if (fileUrl == null || fileUrl.isEmpty() || !fileUrl.contains("filename=")) {
			return;
		}

		try {
			// URL에서 실제 파일 이름(UUID.png)을 추출합니다.
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("=") + 1);

			// 실제 파일 경로를 조합합니다. File.separator는 OS에 맞는 경로 구분자(\ 또는 /)를 사용합니다.
			File file = new File(uploadPath + File.separator + fileName);

			// 파일이 존재하면 삭제합니다.
			if (file.exists()) {
				if (file.delete()) {
					log.info("원래!@!파일 !@!삭제 !@!성공: {}", fileName);
				} else {
					log.warn("기존 프로필 파일 삭제 실패: {}", fileName);
				}
			} else {
				log.warn("삭제할 파일이 존재하지 않습니다: {}", fileName);
			}
		} catch (Exception e) {
			// 파일 삭제 중 오류가 발생해도 전체 프로세스가 멈추지 않도록 로그만 남깁니다.
			log.error("파일 삭제 중 오류 발생", e);
		}
	}
}
