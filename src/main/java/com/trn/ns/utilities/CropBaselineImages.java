package com.trn.ns.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CropBaselineImages {

	//path of folder where images to be cropped are placed [the ending slash needs to  be there]
	//the images after cropping will be placed in the same folder and will replace the original images

	static String dirPath = "src/test/resources/com/croppedImages/";



	static void cropImages(String folderPath) throws IOException {
		int[] coordinates = {0,0,128,122}; //coordinates for cropping
		String[] files = getFileNamesFromDirectory(folderPath);
		for(int i=0;i<files.length;i++) {
			String file = files[i];
			cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], dirPath + file, dirPath + file);
		}
	}

	public static void cropImage(int xOffset, int yOffset, int xDistance, int yDistance, String srcFile, String destFile) throws IOException {
		BufferedImage originalImgage = ImageIO.read(new File(srcFile));
		BufferedImage subImgage = originalImgage.getSubimage(xOffset, yOffset, xDistance, yDistance);
		ImageIO.write(subImgage, "png", new File(destFile));
	}

	public static String[] getFileNamesFromDirectory(String directoryPath){
		File[] listOfFiles = null;
		String[] filenames = null;
		File folder = new File(directoryPath);
		listOfFiles = folder.listFiles();
		filenames = new String[listOfFiles.length];
		for(int i=0;i<listOfFiles.length;i++) {
			filenames[i] = listOfFiles[i].getName();
		}
		return filenames;
	}
}
