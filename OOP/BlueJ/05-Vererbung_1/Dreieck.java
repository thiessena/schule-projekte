
public class Dreieck
{
    private Punkt position;
    
    private double richtung;
    
    private Punkt punktB;
    
    private Punkt punktC;

    public Dreieck( Punkt pPosition, double pRichtung, 
            Punkt pPunktB, Punkt pPunktC ) {
        position = pPosition;
        richtung = pRichtung;
        punktB = pPunktB;
        punktC = pPunktC;
    }

    public void setPosition( Punkt pPosition ) {
        position = pPosition;
    }
    
    public Punkt getPosition() {
        return position;
    }

    public void setRichtung( double pRichtung ) {
        richtung = pRichtung;
    }
    
    public double getRichtung() {
        return richtung;
    }

    public void setPunktB( Punkt pPunktB ) {
        punktB = pPunktB;
    }
    
    public Punkt getPunktB() {
        return punktB;
    }

    public void setPunktC( Punkt pPunktC ) {
        punktC = pPunktC;
    }
    
    public Punkt getPunktC() {
        return punktC;
    }
    
    public void versetzen( int pX, int pY ) {
        position.setX(pX);
        position.setY(pY);
    }
    
    public void drehen( double pGrad ) {
        richtung += pGrad;
        if( richtung >= 360 ) {
            richtung = richtung - 360;
        } else if( richtung < 0 ) {
            richtung = 360 + richtung;
        }
    }
}
