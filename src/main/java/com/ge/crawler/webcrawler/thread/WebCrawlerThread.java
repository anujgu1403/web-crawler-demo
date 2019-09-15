package com.ge.crawler.webcrawler.thread;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.crawler.webcrawler.util.WebCrawlerConstants;

/**
 * @author: anuj kumar
 * 
 *          This class is to create a thread which has logic of web crawler
 *          process and can be used in multi-threading
 * 
 */
public class WebCrawlerThread extends Thread {

	// Declared set to hold unqiue values of each types of pages
	Set<String> addressNodeList = new HashSet<String>();
	Set<String> errorPagesList = new HashSet<String>();
	Set<String> skippedPagesList = new HashSet<String>();
	Set<String> visitedPagesList = new HashSet<String>();
	String searchKeyword = "";
	JsonNode rootNode = null;
	JsonNode pagesNode = null;
	String filePath = "";

	public WebCrawlerThread(String searchKeyword, String filePath) {
		this.searchKeyword = searchKeyword;
		this.filePath = filePath;
	}

	/**
	 * This method is to read the top pages node from provided JSON
	 */
	public void readTopNodeFromJson() {
		try {
			if (null != filePath && !filePath.isEmpty()) {
				byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
				ObjectMapper objectMapper = new ObjectMapper();
				rootNode = objectMapper.readTree(jsonData);

				// To read the main pages node from json which will be used for further
				// processing
				pagesNode = rootNode.get(WebCrawlerConstants.PAGE_NODE);
			} else {
				System.out.println("JSON file path is not defined.");
			}

		} catch (Exception e) {
			System.out.println("Error while reading top node from json: " + e.getMessage());
		}
	}

	/**
	 * run method is overriden to invoke other methods which process Node JSON
	 */
	@Override
	public void run() {
		readTopNodeFromJson();
		if (null != pagesNode)
			fillAllAddressNodesFromJson(pagesNode);
		if (null != pagesNode && !searchKeyword.isEmpty())
			searchLinkedPagesForGivenPage(pagesNode, searchKeyword);
		displayResultLists();
	}

	/**
	 * This method is to search the specific based on given keyword JSON
	 * 
	 * @param JsonNode
	 * @param String
	 */
	public void searchLinkedPagesForGivenPage(JsonNode jsonNode, String searchKeyword) {

		// To search if given page link node is exists in all josn nodes it not adding
		// it into error node list
		if (!searchKeyword.isEmpty()) {
			if (!addressNodeList.contains(searchKeyword)) {
				errorPagesList.add(searchKeyword);
			}
		}

		// To process child node to find all linked nodes using recursion
		Iterator<JsonNode> pagesNodeItr = jsonNode.elements();
		pagesNodeItr.forEachRemaining(node -> {
			pagesNodeItr.next();
			if (node.get(WebCrawlerConstants.ADDRESS_NODE).asText().equals(searchKeyword)) {
				if (!visitedPagesList.contains(node.get(WebCrawlerConstants.ADDRESS_NODE).asText())) {

					// If node is not already exists in visited pages list then adding that in it
					visitedPagesList.add(node.get(WebCrawlerConstants.ADDRESS_NODE).asText());
					JsonNode linksNode = node.get(WebCrawlerConstants.LINK_NODE);
					Iterator<JsonNode> links = linksNode.elements();
					links.forEachRemaining(linkNode -> {
						links.next();

						// To call search method recursively to find other linked node
						searchLinkedPagesForGivenPage(jsonNode, linkNode.asText());
					});
				} else {
					// If node is already visited adding it in skipped node list
					skippedPagesList.add(node.get(WebCrawlerConstants.ADDRESS_NODE).asText());
				}
			}

		});
	}

	/**
	 * This method is fill all address nodes in address nodes list
	 * 
	 * @param JsonNode
	 */
	public void fillAllAddressNodesFromJson(JsonNode jsonNode) {
		Iterator<JsonNode> pagesNodeItr = jsonNode.elements();
		pagesNodeItr.forEachRemaining(node -> {
			pagesNodeItr.next();
			addressNodeList.add(node.get(WebCrawlerConstants.ADDRESS_NODE).asText());
		});
	}

	/**
	 * This method is to display all the node which are processed after JOSN
	 * processing
	 * 
	 */
	public void displayResultLists() {
		System.out.println("Success: " + visitedPagesList);
		System.out.println("Skipped: " + skippedPagesList);
		System.out.println("Error: " + errorPagesList);
		System.out.println("****************************************");
	}
}
