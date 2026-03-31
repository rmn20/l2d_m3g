package com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.m3g.Image2D;

class BMPLoader {
	
	private final byte[] data;
	private int pos;

	BMPLoader(byte[] bytes) { data = bytes; }
	
	final void skip(int bytes) {
		pos += bytes;
	}

	final byte readByte() {
		return data[pos++];
	}

	final int readUByte() {
		return data[pos++] & 0xff;
	}

	final short readShort() {
		return (short) ((data[pos++] & 0xff) | ((data[pos++] & 0xff) << 8));
	}

	final int readUShort() {
		return (data[pos++] & 0xff) | ((data[pos++] & 0xff) << 8);
	}

	final int readInt() { 
		return readUShort() | (readUShort() << 16); 
	}
	
	public static final Image2D loadBMP(String name) throws IOException {
		if(name == null) throw new NullPointerException();

		InputStream is = (new Object()).getClass().getResourceAsStream(name);
		if(is == null) throw new IOException("Resource not found: " + name);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[Math.max(1024, is.available())];
		
		int len;
		while((len = is.read(buffer)) > 0) {
			baos.write(buffer, 0, len);
		}
		
		is.close();

		return loadBMP(name, baos.toByteArray());
	}
	private static final int BMP_FILE_HEADER_SIZE = 0x0E;
	private static final int BMP_INFO_HEADER_V2_SIZE = 12;
	private static final int BMP_INFO_HEADER_V3_SIZE = 40;

	private static final Image2D loadBMP(String name, byte[] data) {
		BMPLoader loader = new BMPLoader(data);
		//BITMAPFILEHEADER
		if(loader.readUByte() != 'B' || loader.readUByte() != 'M') {
			throw new RuntimeException("Not a BMP file: " + name);
		}
		
		//"bm", file size 4b, reserved 4b
		loader.skip(BMP_FILE_HEADER_SIZE - 6);

		int rasterOffset = loader.readInt();
		//BITMAPINFOHEADER
		int headerSize = loader.readInt();
		if(headerSize != BMP_INFO_HEADER_V2_SIZE && headerSize != BMP_INFO_HEADER_V3_SIZE) {
			throw new RuntimeException("Invalid BMP header size: " + name);
		}
		
		int width = loader.readInt();
		int height = loader.readInt();
		if(width <= 0 || height == 0) {
			throw new RuntimeException("Invalid BMP resolution: " + name);
		}
		
		boolean reversed = height >= 0;
		height = Math.abs(height);

		if(loader.readUShort() != 1) {
			throw new RuntimeException("BMP planes != 1: " + name);
		}

		if(loader.readUShort() != 8) {
			throw new RuntimeException("BMP bpp != 8: " + name);
		}
		
		int numColors;

		if(headerSize == BMP_INFO_HEADER_V2_SIZE) {
			numColors = 256;
		} else {
			int compression = loader.readInt();
			if(compression != 0) {
				throw new RuntimeException("BMP compression is not supported: " + name);
			}

			loader.skip(12);
			numColors = loader.readInt();
			if(numColors <= 0) numColors = 256; 
			loader.skip(4);
		}

		int paletteOffset = BMP_FILE_HEADER_SIZE + headerSize;
		if(rasterOffset < paletteOffset + numColors * 4) rasterOffset = paletteOffset + numColors * 4;
		
		byte[] palette = new byte[256 * 3];
		
		for(int i = 0; i < numColors; i++) {
			int idx = i * 4 + paletteOffset;
			
			palette[i * 3    ] = data[idx + 2];
			palette[i * 3 + 1] = data[idx + 1];
			palette[i * 3 + 2] = data[idx    ];
		}
		
		int padding = width & 3;
		int stride = padding == 0 ? width : width + 4 - padding;
		byte[] bitmap;
		
		if(reversed) {
			bitmap = new byte[width * height];
			
			for(int y = 0; y < height; y++) {
				System.arraycopy(data, rasterOffset + y * stride, bitmap, (height - 1 - y) * width, width);
			}
		} else {
			bitmap = data;
			
			for(int y = 0; y < height; y++) {
				System.arraycopy(data, rasterOffset + y * stride, bitmap, y * width, width);
			}
		}
		
		return new Image2D(Image2D.RGB, width, height, bitmap, palette);
	}
}
