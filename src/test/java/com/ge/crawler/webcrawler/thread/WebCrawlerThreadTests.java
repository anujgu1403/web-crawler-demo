package com.ge.crawler.webcrawler.thread;

import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.crawler.webcrawler.util.WebCrawlerConstants;


/**
 * @author: anuj kumar
 * 
 * This class is to write junit test cases for thread class
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebCrawlerThreadTests {
	
	@InjectMocks
	WebCrawlerThread webCThread;

	JsonNode pagesNode = null;
	String searchKeyword = "page-50";
	String jsonFilePath = "C:\\web\\internet.json";
	

	@Test
	public void readTopNodeFromJsonTest() throws IOException {		
		initailNode();
		webCThread.readTopNodeFromJson();
		assertNotNull(pagesNode);		
	}
	
	@Test
	public void searchLinkedPagesForGivenPageTest() throws IOException {
		initailNode();
		webCThread.searchLinkedPagesForGivenPage(pagesNode, searchKeyword);
		assertNotNull(webCThread.errorPagesList);
		assertNotNull(webCThread.skippedPagesList);
		assertNotNull(webCThread.visitedPagesList);		
	}
	
	@Test
	public void runTest() throws IOException {
		initailNode();
		webCThread.readTopNodeFromJson();
		webCThread.searchLinkedPagesForGivenPage(pagesNode, searchKeyword);
		assertNotNull(webCThread.errorPagesList);
		assertNotNull(webCThread.skippedPagesList);
		assertNotNull(webCThread.visitedPagesList);	
	}
	
	@Test
	public void fillAllAddressNodesFromJsonTest() throws IOException {
		initailNode();
		webCThread.fillAllAddressNodesFromJson(pagesNode);
		assertNotNull(webCThread.addressNodeList);
	}
		
	public void initailNode() throws IOException {
		byte[] jsonData = Files.readAllBytes(Paths.get(jsonFilePath));
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonData);

		// To read the main pages node from JSON which will be used for further
		// processing
		pagesNode = rootNode.get(WebCrawlerConstants.PAGE_NODE);		
	}
}
