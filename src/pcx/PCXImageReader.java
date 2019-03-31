// PCXImageReader.java

package pcx;

import java.awt.*;
import java.awt.image.*;

import java.io.*;

import java.util.*;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.spi.*;
import javax.imageio.stream.*;

public class PCXImageReader extends ImageReader
{
   // image width (in pixels) -- returned from getWidth()

   private int width;

   // image height (in pixels) -- returned from getHeight()

   private int height;

   // image bits per pixel -- must be 1, 4, or 8

   private int bitsPerPixel;

   // image planes -- must be 1 or 3

   private int nPlanes;

   // number of bytes per line per plane -- must be an even number

   private int bytesPerLine;

   // bytesPerLine * nPlanes

   private int scanlineLength;

   // storage location for each read scanline

   private byte [] scanline;

   // merged EGA and VGA palettes

   private byte [] palette = new byte [256*3];

   // input source as an ImageInputStream

   private ImageInputStream stream;

   // flag to prevent the same header from being read multiple times

   private boolean gotHeader;

   public PCXImageReader (ImageReaderSpi originatingProvider)
   {
      // Save the identity of the ImageReaderSpi subclass that invoked this
      // constructor.

      super (originatingProvider);
   }

   public int getHeight (int imageIndex) throws IIOException
   {
      // Because a PCX file contains only one image, imageIndex must be zero.
      // The private checkIndex() method throws IndexOutOfBoundsException if
      // imageIndex is not 0.

      checkIndex (imageIndex);

      // To get the height, the image header must be read. The private
      // readHeader() method does nothing if the header has been read.

      readHeader ();

      return height;
   }

   public IIOMetadata getImageMetadata (int imageIndex)
   {
      // This plug-in ignores image metadata -- metadata about a single image.

      return null;
   }

   public Iterator<ImageTypeSpecifier> getImageTypes (int imageIndex)
      throws IIOException
   {
      checkIndex (imageIndex);

      readHeader ();

      // Create a List of ImageTypeSpecifiers that identify the possible image
      // types to which the single PCX image can be decoded. An
      // ImageTypeSpecifier is used with ImageReader's getDestination() method
      // to return an appropriate BufferedImage that contains the decoded
      // image, and is accessed by an application.

      java.util.List<ImageTypeSpecifier> l;
      l = new ArrayList<ImageTypeSpecifier> ();

      // The PCX reader only uses a single List entry. This entry describes a
      // BufferedImage of TYPE_INT_RGB, which is a commonly used image type.

      l.add (ImageTypeSpecifier.
             createFromBufferedImageType (BufferedImage.TYPE_INT_RGB));

      // Return an iterator that retrieves elements from the list.

      return l.iterator ();
   }

   public int getNumImages (boolean allowSearch)
   {
      // A PCX file stores only one image.

      return 1;
   }

   public IIOMetadata getStreamMetadata ()
   {
      // This plug-in ignores stream metadata -- metadata about all images in
      // an ImageInputStream or other input source.

      return null;
   }

   public int getWidth (int imageIndex) throws IIOException
   {
      checkIndex (imageIndex);

      readHeader ();

      return width;
   }

   public BufferedImage read (int imageIndex, ImageReadParam param)
      throws IOException
   {
      checkIndex (imageIndex);

      readHeader ();

      // Calculate and return a Rectangle that identifies the region of the
      // source image that should be read:
      //
      // 1. If param is null, the upper-left corner of the region is (0, 0),
      //    and the width and height are specified by the width and height
      //    arguments. In other words, the entire image is read.
      //
      // 2. If param is not null
      //
      //    2.1 If param.getSourceRegion() returns a non-null Rectangle, the
      //        region is calculated as the intersection of param's Rectangle
      //        and the earlier (0, 0, width, height Rectangle).
      //
      //    2.2 param.getSubsamplingXOffset() is added to the region's x
      //        coordinate and subtracted from its width.
      //
      //    2.3 param.getSubsamplingYOffset() is added to the region's y
      //        coordinate and subtracted from its height.

      Rectangle sourceRegion = getSourceRegion (param, width, height);

      // Source subsampling is used to return a scaled-down source image.
      // Default 1 values for X and Y subsampling indicate that a non-scaled
      // source image will be returned.

      int sourceXSubsampling = 1;
      int sourceYSubsampling = 1;

      // The final step in reading an image from a source to a destination is
      // to map the source samples in various source bands to destination
      // samples in various destination bands. This lets you return only the
      // red component of an image, for example. Default null values indicate
      // that all source and destination bands are used.

      int [] sourceBands = null;
      int [] destinationBands = null;

      // The destination offset determines the starting location in the
      // destination where decoded pixels are placed. Default (0, 0) values
      // indicate the upper-left corner.

      Point destinationOffset = new Point (0, 0);

      // If param is not null, override the source subsampling, source bands,
      // destination bands, and destination offset defaults.

      if (param != null)
      {
          sourceXSubsampling = param.getSourceXSubsampling ();
          sourceYSubsampling = param.getSourceYSubsampling ();
          sourceBands = param.getSourceBands ();
          destinationBands = param.getDestinationBands ();
          destinationOffset = param.getDestinationOffset ();
      }

      // Obtain a BufferedImage into which decoded pixels will be placed. This
      // destination will be returned to the application.
      //
      // 1. If param is not null
      //
      //    1.1 If param.getDestination() returns a BufferedImage 
      //
      //        1.1.1 Return this BufferedImage
      //
      //        Else
      //
      //        1.1.2 Invoke param.getDestinationType ().
      //
      //        1.1.3 If the returned ImageTypeSpecifier equals 
      //              getImageTypes (0) (see below), return its BufferedImage.
      //
      // 2. If param is null or a BufferedImage has not been obtained
      //
      //    2.1 Return getImageTypes (0)'s BufferedImage.

      BufferedImage dst = getDestination (param, getImageTypes (0), width,
                                          height);

      // Verify that the number of source bands and destination bands, as
      // specified by param, are the same. If param is null, 3 is compared
      // with dst.getSampleModel ().getNumBands (), which must also equal 3.
      // An IllegalArgumentException is thrown if the number of source bands
      // differs from the number of destination bands.

      checkReadParamBandSettings (param, 3,
                                  dst.getSampleModel ().getNumBands ());

      // Create a WritableRaster for the source.

      WritableRaster wrSrc = Raster.createBandedRaster (DataBuffer.TYPE_BYTE,
                                                        width, 1, 3,
                                                        new Point (0, 0));
      byte [][] banks;
      banks = ((DataBufferByte) wrSrc.getDataBuffer ()).getBankData ();

      // Create a WritableRaster for the destination.

      WritableRaster wrDst = dst.getRaster ();

      // Identify destination rectangle for clipping purposes. Only source
      // pixels within this rectangle are copied to the destination.

      int dstMinX = wrDst.getMinX ();
      int dstMaxX = dstMinX + wrDst.getWidth () - 1;
      int dstMinY = wrDst.getMinY ();
      int dstMaxY = dstMinY + wrDst.getHeight () - 1;

      // Create a child raster that exposes only the desired source bands.

      if (sourceBands != null)
          wrSrc = wrSrc.createWritableChild (0, 0, width, 1, 0, 0,
                                             sourceBands);

      // Create a child raster that exposes only the desired destination
      // bands.

      if (destinationBands != null)
          wrDst = wrDst.createWritableChild (0, 0, wrDst.getWidth (),
                                             wrDst.getHeight (), 0, 0,
                                             destinationBands);

      int srcY = 0;
      try
      {
          int [] pixel = wrSrc.getPixel (0, 0, (int []) null);

          for (srcY = 0; srcY < height; srcY++)
          {
               // Read the next row from the PCX file.

               readScanline ();
               copyScanlineToBanks (banks);

               // Reject rows that lie outside the source region, or which are
               // not part of the subsampling.

               if ((srcY < sourceRegion.y) ||
                   (srcY >= sourceRegion.y + sourceRegion.height) ||
                   (((srcY - sourceRegion.y) % sourceYSubsampling) != 0))
                   continue;

               // Determine the row's location in the destination.

               int dstY = destinationOffset.y +
                          (srcY - sourceRegion.y)/sourceYSubsampling;
               if (dstY < dstMinY)
                   continue; // The row is above the top of the destination
                             // rectangle.

               if (dstY > dstMaxY)
                   break; // The row is below the bottom of the destination
                          // rectangle.

               // Copy each subsampled source pixel that fits into the
               // destination rectangle into the destination.

               for (int srcX = sourceRegion.x;
                    srcX < sourceRegion.x + sourceRegion.width; srcX++)
               {
                    if (((srcX - sourceRegion.x) % sourceXSubsampling) != 0)
                        continue;

                    int dstX = destinationOffset.x +
                               (srcX - sourceRegion.x)/sourceXSubsampling;
                    if (dstX < dstMinX)
                        continue; // The pixel is to the destination
                                  // rectangle's left.

                    if (dstX > dstMaxX)
                        break; // The pixel is to the destination rectangle's
                               // right.

                    // Copy the pixel. Sub-banding is automatically handled.

                    wrSrc.getPixel (srcX, 0, pixel);
                    wrDst.setPixel (dstX, dstY, pixel);
               }
          }
      }
      catch (IOException e)
      {
         throw new IIOException ("Error reading line " + srcY +
                                 ": " + e.getMessage (), e);
      }

      return dst;
   }

   public void setInput (Object input, boolean seekForwardOnly,
                         boolean ignoreMetadata)
   {
      super.setInput (input, seekForwardOnly, ignoreMetadata);

      if (input == null)
      {
          this.stream = null;
          return;
      }

      // The input source must be an ImageInputStream because the originating
      // provider -- the PCXImageReaderSpi class -- passes STANDARD_INPUT_TYPE
      // -- an array consisting only of ImageInputStream -- to its superclass
      // in its constructor call.

      if (input instanceof ImageInputStream)
          this.stream = (ImageInputStream) input;
      else
          throw new IllegalArgumentException ("ImageInputStream expected!");
   }

   // ==============
   // HELPER METHODS
   // ==============

   private void checkIndex (int imageIndex)
   {
      if (imageIndex != 0)
          throw new IndexOutOfBoundsException ("Bad index!");
   }

   private void copyScanlineToBanks (byte [][] banks)
   {
      if (bitsPerPixel == 1 && nPlanes == 1)
      {
          byte [] mask = { -128, 64, 32, 16, 8, 4, 2, 1 };

          for (int i = 0; i < width; i++)
          {
               byte _byte = scanline [i >> 3];
               boolean bitSet = ((_byte & mask [i & 7]) != 0) ? true : false;
               banks [0][i] = palette [!bitSet ? 0 : 3];
               banks [1][i] = palette [!bitSet ? 1 : 4];
               banks [2][i] = palette [!bitSet ? 2 : 5];
          }
      }
      else
      if (bitsPerPixel == 4 && nPlanes == 1)
      {
          for (int i = 0; i < width; i++)
          {
               int index;
               if ((i & 1) == 0)
                   index = (scanline [i >> 1] >> 4) & 15;
               else
                   index = scanline [i >> 1] & 15;
               index += (index + index);
               banks [0][i] = palette [index++];
               banks [1][i] = palette [index++];
               banks [2][i] = palette [index];
          }
      }
      else
      if (bitsPerPixel == 8 && nPlanes == 1)
      {
          for (int i = 0; i < width; i++)
          {
               int index = scanline [i] & 255;
               index += (index + index);
               banks [0][i] = palette [index++];
               banks [1][i] = palette [index++];
               banks [2][i] = palette [index];
          }
      }
      else
      {
          System.arraycopy (scanline, 0, banks [0], 0, width);
          System.arraycopy (scanline, bytesPerLine, banks [1], 0, width);
          System.arraycopy (scanline, bytesPerLine*2, banks [2], 0, width);
      }
   }

   private void readHeader () throws IIOException
   {
      // Do not allow this header to be read more than once.

      if (gotHeader)
          return;

      // Make sure that the application has set the input source.

      if (stream == null)
          throw new IllegalStateException ("No input stream!");

      // Read the header.

      byte [] header = new byte [128];
      try
      {
          stream.readFully (header);
      }
      catch (IOException e)
      {
          throw new IIOException ("Unable to read header!", e);
      }

      // Validate the header.

      if (header [0] != 10)
          throw new IIOException ("PCX Manufacturer value incorrect!");

      if (header [1] != 5)
          throw new IIOException ("PCX Version value incorrect!");

      if (header [2] != 1)
          throw new IIOException ("PCX Encoding value incorrect!");

      if (header [3] != 1 && header [3] != 4 && header [3] != 8)
          throw new IIOException ("PCX BitsPerPixel value incorrect!");

      // Verify NPlanes field.

      if (header [65] != 1 && header [65] != 3)
          throw new IIOException ("PCX NPlanes value incorrect!");

      if ((header [3] == 1 || header [3] == 4) && header [65] == 3)
          throw new IIOException ("PCX BitsPerPixel/NPlanes mismatch!");

      if ((header [66] & 1) == 1) // Is field's least-significant byte odd?
          throw new IIOException ("PCX BytesPerLine value must be even!");

      // Obtain information from header.

      int tmp1 = header [5] & 255;
      int tmp2 = header [4] & 255;
      int xmin = (tmp1 << 8) | tmp2;

      tmp1 = header [7] & 255;
      tmp2 = header [6] & 255;
      int ymin = (tmp1 << 8) | tmp2;

      tmp1 = header [9] & 255;
      tmp2 = header [8] & 255;
      int xmax = (tmp1 << 8) | tmp2;

      tmp1 = header [11] & 255;
      tmp2 = header [10] & 255;
      int ymax = (tmp1 << 8) | tmp2;

      width = xmax-xmin+1;
      height = ymax-ymin+1;

      bitsPerPixel = header [3];

      nPlanes = header [65];

      tmp1 = header [67] & 255;
      tmp2 = header [66] & 255;
      bytesPerLine = (tmp1 << 8) | tmp2;

      scanlineLength = nPlanes * bytesPerLine;
      scanline = new byte [scanlineLength];

      // Obtain appropriate palette.

      if (bitsPerPixel == 8 && nPlanes == 1)
      {
          try
          {
              stream.mark ();
       
              // Seek to the end of the file.

              stream.seek (stream.length ());

              // Seek backwards past the VGA palette (which must be present)
              // to the byte containing a decimal 12 -- the VGA palette
              // indicator byte.

              stream.seek (stream.getStreamPosition () - 769);

              // If VGA palette is present then read it into palette array.

              if (stream.readUnsignedByte () != 12)
                  throw new IOException ();

              stream.read (palette);

              stream.reset ();
          }
          catch (IOException e)
          {
              throw new IIOException ("No VGA palette or read error!");
          }
      }
      else
      {
          // Copy EGA palette from header to palette.

          System.arraycopy (header, 16, palette, 0, 48);
      }

      gotHeader = true;
   }

   private void readScanline () throws IOException
   {
      int index = 0;

      // Read a single scanline into the scanline array.

      do
      {
          int x = stream.readUnsignedByte ();
          if ((x & 0xc0) == 0xc0)
          {
              int count = x & 0x3f;
              if (count == 0)
                  throw new IOException ("Zero run count detected!");

              if (index+count-1 >= scanlineLength)
                  throw new IOException ("Scanline overflow!");

              x = stream.readUnsignedByte ();
              for (int i = 1; i <= count; i++)
                   scanline [index++] = (byte) x;
          }
          else
              scanline [index++] = (byte) x;
      }
      while (index < scanlineLength);
   }
}
