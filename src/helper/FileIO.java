package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileIO {
	public static File findSiblingResource(Class<?> cls, String FileName) {
		String path = null;
		try {
			//如果不用toURI，当文件路径中有空格时，以下方法就会出错，因为getResource返回的URL会把空格转化成 %20
			URL url=cls.getResource(FileName);
			path = url.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File file = new File(path);
		return file;
	}

	public static String readFile(File f, Charset encoding) {
		String path = f.getAbsolutePath();
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String content = encoding.decode(ByteBuffer.wrap(encoded)).toString();
		Msg.userMsgLn("你的SQL是: \n" + content);
		return content;
	}

	public static SqlResults compactSQLFromFile(File f) {
		Scanner sc = null;
		SqlResults sqlResults = new SqlResults();
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNextLine()) {
			String l = sc.nextLine().trim();
			// ignore comments and empty line
			if (l.startsWith("--") || l.isEmpty()) {
				continue;
			}
			sqlResults.compat.append(l + " ");
			sqlResults.full.append(l + "\n");
		}
		sc.close();
		return sqlResults;
	}

	public static class SqlResults {
		public StringBuilder compat = new StringBuilder();
		public StringBuilder full = new StringBuilder();
	}
}
