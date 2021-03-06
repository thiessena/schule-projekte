
/**
 * Implementierung der Tiefensuche auf einem ungereichteten,
 * gewichteten Graphen.
 */
public class Breitensuche {

    private Graph g;

    public Breitensuche() {
        g = new Graph();

        // Aufbau des Graphen
        // Erstellen der Knoten (Vertices)
        Vertex koe = new Vertex("Köln");
        g.addVertex(koe);
        Vertex dues = new Vertex("Düsseldorf");
        g.addVertex(dues);
        Vertex dor = new Vertex("Dortmund");
        g.addVertex(dor);
        Vertex bi = new Vertex("Bielefeld");
        g.addVertex(bi);
        Vertex hnv = new Vertex("Hannover");
        g.addVertex(hnv);
        Vertex bo = new Vertex("Bochum");
        g.addVertex(bo);

        // Erstellen der Kanten (Edges)
        g.addEdge(new Edge(koe, dor, 96.0));
        g.addEdge(new Edge(dues, dor, 70.0));
        g.addEdge(new Edge(dor, bo, 22.0));
        g.addEdge(new Edge(bi, hnv, 113.0));
        g.addEdge(new Edge(bi, dor, 112.0));
        g.addEdge(new Edge(bi, dues, 178.0));
        g.addEdge(new Edge(bo, dues, 52.0));
    }

    /**
     * Suche nach einem Vertex mit der angegebenen ID mittels der Breitensuche.
     * @param pVertexID
     * @return
     */
    public boolean findVertex( String pVertexID ) {
        g.setAllVertexMarks(false); // Markierungen zurücksetzen
        Queue<Vertex> searchQueue = new Queue<>(); // Speicher für zu bearbeitende Knoten erstellen

        // TODO: Implementiere die Breitensuche als iterativen Algorithmus:
        //       Reihe dazu die noch nicht markierten Nachbarknoten in die
        //       searchQueue ein und arbeite die Knoten in ihr der Reihe nach
        //       ab, bis die Queue leer ist, oder der gesuchte Knoten
        //       gefunden wurde.
        // TODO: Ergänze deine Methode um Ausgaben, anhand derer die Abarbeitung
        //       deutlich wird.
        // TODO: Wenn dein Algorithmus funnktioniert, kopiere die Methode und
        //       Erstelle Varianten, bei denen die Reihenfolge der Nachbarknoten
        //       modifiziert ist. Dazu
        return false;
    }

    /**
     *
     * @param pVertexID
     * @return
     */
    public boolean findVertexByID( String pVertexID ) {
        g.setAllVertexMarks(false); // Markierungen zurücksetzen
        Queue<Vertex> searchQueue = new Queue<>(); // Speicher für zu bearbeitende Knoten erstellen

        // TODO: Ändere die Breitensuche so ab, dass die Knoten so abgearbeitet
        //       werden, dass die Nachbarknoten in alphabetischer Reihenfolge
        //       besucht werden.
        //       Nutze dazu die Hilfsmethode getVertexFromListByID, um aus
        //       Liste der Nachbarknoten denjenigen, der alphabetisch als
        //       erstes kommt herauszusuchen.
        return false;
    }

    public boolean findVertexByWeight( String pVertexID ) {
        g.setAllVertexMarks(false); // Markierungen zurücksetzen
        Queue<Vertex> searchQueue = new Queue<>(); // Speicher für zu bearbeitende Knoten erstellen

        // TODO: Ändere die Breitensuche so ab, dass die Knoten so abgearbeitet
        //       werden, dass der Nachbarknoten mit der Kante mit dem geringsten
        //       Gewicht zuerst besucht wird.
        //       Nutze dazu die Hilfsmethode getVertexFromListByWeight, um aus
        //       Liste der Nachbarknoten denjenigen mit dem geringsten Gewicht
        //       herauszusuchen.
        return false;
    }


    /**
     * Sucht aus einer Liste von Knoten denjenigen, dessen ID alphabetisch ale
     * erstes kommt. Der Knoten wird aus der Liste gelöscht und dann zurück
     * gegeben.
     * @param pVertices
     * @return
     */
    private Vertex getVertexFromListByID( List<Vertex> pVertices ) {
        pVertices.toFirst();
        Vertex v = pVertices.getContent();
        do {
          pVertices.next();

          if( pVertices.hasAccess() &&
              pVertices.getContent().getID().compareToIgnoreCase(v.getID()) < 0 ) {
              v = pVertices.getContent();
          }
        } while( pVertices.hasAccess() );

        pVertices.toFirst();
        while( pVertices.hasAccess() ) {
            if( pVertices.getContent().getID().equals(v.getID()) ) {
                break;
            }
            pVertices.next();
        }
        pVertices.remove();
        return v;
    }

    /**
     * Sucht aus einer Liste von Knoten denjenigen, dessen Kantengewicht zum
     * Konten <code>pCurrentVertex</code> am geringsten ist. Der Knoten wird
     * aus der Liste gelöscht und dann zurück
     * gegeben.
     * @param pVertices
     * @return
     */
    private Vertex getVertexFromListByWeight( Vertex pCurrentVertex, List<Vertex> pVertices ) {
        pVertices.toFirst();
        Vertex v = pVertices.getContent();
        double weight = g.getEdge(pCurrentVertex, v).getWeight();
        do {
            pVertices.next();

            if( pVertices.hasAccess() &&
                g.getEdge(pCurrentVertex, pVertices.getContent()).getWeight() < weight ) {
                v = pVertices.getContent();
            }
        } while( pVertices.hasAccess() );

        pVertices.toFirst();
        while( pVertices.hasAccess() ) {
            if( pVertices.getContent().getID().equals(v.getID()) ) {
                break;
            }
            pVertices.next();
        }
        pVertices.remove();
        return v;
    }
}
