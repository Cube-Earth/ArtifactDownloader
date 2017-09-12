package earth.cube.tools.artifactdownloader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Spooler {
	
	private final static int BUF_SIZE = 0x1000;
	
	public static void spool(InputStream in, OutputStream out, boolean bCloseIn, boolean bCloseOut) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		try {
			int n = in.read(buf);
			while(n != -1) {
				out.write(buf, 0, n);
				n = in.read(buf);
			}
		}
		finally {
			if(bCloseIn)
				try {
					in.close();
				} catch (IOException e) {
				}
			
			if(bCloseOut)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
	}

}
