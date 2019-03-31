// PCXImageReaderSpi.java

package pcx;

import java.io.IOException;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

public class PCXImageReaderSpi extends ImageReaderSpi
{
   public PCXImageReaderSpi ()
   {
      super ("JavaJeff",          // vendorName
             "1.0",               // version
             new String [] { "PCX" }, // names
             new String [] { "pcx" }, // suffixes
             new String [] { "image/x-pcx" }, // MIMETypes
             "ca.mb.javajeff.pcx.PCXImageReader", // readerClassName
             new Class[] {ImageInputStream.class}, // inputTypes
             null,                // writerSpiNames
             false,               // supportsStandardStreamMetadataFormat
             null,                // nativeStreamMetadataFormatName
             null,                // nativeStreamMetadataFormatClassName
             null,                // extraStreamMetadataFormatNames
             null,                // extraStreamMetadataFormatClassNames
             false,               // supportsStandardImageMetadataFormat
             null,                // nativeImageMetadataFormatName
             null,                // nativeImageMetadataFormatClassName
             null,                // extraImageMetadataFormatNames
             null);               // extraImageMetadataFormatClassNames
   }

   // ImageIO's getImageReaders(Object input) method is the only ImageIO
   // method to call canDecodeInput(Object input).

   public boolean canDecodeInput (Object input) throws IOException
   {
      // The input source must be an ImageInputStream because the constructor
      // passes STANDARD_INPUT_TYPE (an array consisting of ImageInputStream)
      // as the only type of input source that it will deal with to its
      // superclass.

      if (!(input instanceof ImageInputStream))
          return false;

      ImageInputStream stream = (ImageInputStream) input;

      // Read and validate the input source's header.

      byte [] header = new byte [128];
      try
      {
          // The input source's current position must be preserved so that
          // other ImageReaderSpis can determine if they can decode the input
          // source's format, should this input source be unable to handle the 
          // decoding. Because the input source is an ImageInputStream, its
          // mark() and reset() methods are called to preserve the current
          // position.

          stream.mark ();
          stream.readFully (header);
          stream.reset ();
      }
      catch (IOException e)
      {
          return false;
      }

      // Verify Manufacturer field.

      if (header [0] != 10)
          return false;

      // Verify Version field.

      if (header [1] != 5)
          return false;

      // Verify Encoding field.

      if (header [2] != 1)
          return false;

      // Verify BitsPerPixel field.

      if (header [3] != 1 && header [3] != 4 && header [3] != 8)
          return false;

      // Verify NPlanes field.

      if (header [65] != 1 && header [65] != 3)
          return false;

      // Verify BitsPerPixel with NPlanes.

      if ((header [3] == 1 || header [3] == 4) && header [65] == 3)
          return false;

      // Verify BytesPerLine field.

      if ((header [66] & 1) == 1) // Does field's least-significant byte odd?
          return false;

      // Input source points to a valid PCX file, in so far as the header is
      // concerned.

      return true;
   }

   public ImageReader createReaderInstance (Object extension)
      throws IOException
   {
      // Inform the PCX image reader that this PCX image reader SPI is the
      // originating provider -- the object that creates the PCX image reader.

      return new PCXImageReader (this);
   }

   public String getDescription (java.util.Locale locale)
   {
      // This method should return a localized description.

      return "PCX Reader Plug-in by Jeff Friesen";
   }
}
