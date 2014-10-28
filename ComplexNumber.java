package flinders.mandelbrot;

/**
 * A complex number.
 * @author Martin Luerssen
 */
public class ComplexNumber {
   public double real;
   public double imaginary;

   public ComplexNumber(double _real, double _imaginary) {
      real = _real;
      imaginary = _imaginary;
   }

   // returns the modulus/magnitude of this number
   public double abs() { 
      return Math.sqrt(real*real + imaginary*imaginary);
   }

   // adds another complex number to this one
   public void add(ComplexNumber other) {
      real += other.real;
      imaginary += other.imaginary;
   }

   // multiplies this number by another complex number
   public void multiply(ComplexNumber other) {
      double realSWAP = (real * other.real) - (imaginary * other.imaginary);
      imaginary = (real * other.imaginary) + (imaginary * other.real);
      real = realSWAP;
   }
}
