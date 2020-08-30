import greenfoot.*;

import java.util.Random;
import java.lang.Math;
import java.io.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Die einzigen aktiven Akteure in der Roboterwelt sind die Rover.
 * Die Welt besteht aus 15 * 12 Feldern.
 */
public class Planet extends World {

    private static int zellenGroesse = 50;

    /**
     * Erschaffe eine Welt mit 15 * 12 Zellen.
     */
    public Planet() {
        // Initialisierung der Welt
        super(15, 12, zellenGroesse);
        setBackground("images/boden.png");
        setPaintOrder(String.class, Rover.Display.class, Rover.class, Marke.class, Gestein.class, Huegel.class);
        Greenfoot.setSpeed(20);

        // Diese Zeile auskommentieren, um beim Start eine Zufallskarte zu erstellen.
        //zufallskarte();

        // Diese Zeile auskommentieren, um beim Start eine Karte aus dem Maps-Order zu laden.
        //karteLaden("empty");

        // Diese Zeile auskommentieren, um beim Start eine eigene Karte zu erstellen.
        //weltAusKarteErstellen(".\n.\n....HH.HHH.\n..R..G.GG.G..M\n......HH.HH.H.");
    }

    /**
     * L�scht alle Objekte (Rover,H�gel,Gesteine,Markierungen,...) aus der Welt.
     */
    public void weltLeeren() {
        removeObjects(getObjects(Actor.class));
    }


    /**
     * Es werden Rober, H�gel, Gesteine und Markierungen erstellt.
     * Dazu wird der String Zeilenweise verarbeitet. Jedes Zeichen
     * entspricht einer Zelle in der Welt. Je nachdem, welches
     * Zeichen gelesen wird, werden die verschiedenen Objekte erstellt
     * und platziert.
     * <dl>
     * <dt><code>r</code></dt>
     * <dd>Platziert einen Rover mit Blickrichtung rechts. Alternativ kann <code>&gt;</code> benutzt werden.</dd>
     * <dt><code>&lt;</code></dt>
     * <dd>Platziert einen Rover mit Blickrichtung links.</dd>
     * <dt><code>^</code></dt>
     * <dd>Platziert einen Rover mit Blickrichtung oben.</dd>
     * <dt><code>v</code></dt>
     * <dd>Platziert einen Rover mit Blickrichtung unten.</dd>
     * <dt><code>h</code</dt>
     * <dd>Platziert einen H�gel.</dd>
     * <dt><code>g</code</dt>
     * <dd>Platziert ein Gestein.</dd>
     * <dt><code>m</code</dt>
     * <dd>Platziert eine Marke.</dd>
     * <dt><code>z</code</dt>
     * <dd>Platziert ein zuf�lliges Objekt (oder keines).</dd>
     * </dl>
     * <p>
     * Die Welt hat normalerweise 15-mal-12 Zellen. Das bedeutet, der
     * Karten-String soltle maximal 12 Zeilen und jede Zeile maximal
     * 15 Zeichen enthalten. Alle Zeichen dar�ber hinaus werden ignoriert.
     * Gro�- und Kleinschreibung wird nicht beachtet.
     * <p>
     * Beispiel 1: Eine Welt, die von H�geln umgeben ist, einen Rover mit
     * Blickrichtung oben platziert, ein Gestein und einige Marken.
     * <pre>
     * hhhhhhhhhhhhhhh
     * hOOOOOOOOOOOOOh
     * hOOOOOmOOOmOOOh
     * hOOOOOOOOOOOOOh
     * hOOOOO^OOOOOOOh
     * hOOOOOOOOOOOOOh
     * hOOOOOOOOOOOOOh
     * hOOOOOgOOOmOOOh
     * hOOOOOOOOOOOOOh
     * hOOOOOOOOOOOOOh
     * hhhhhhhhhhhhhhh
     * </pre>
     * <p>
     * Beispiel 2: Es wird der Rover und ein Gestein platziert.
     * <pre>
     *
     *
     *     R     G
     * </pre>
     * <p>
     * Beispiel 3: Es werden der Rover und zwei Gesteine platziert.
     * Der Wassergehalt des ersten Gesteins wird auf 12 festgelegt.
     * <pre>
     *
     *
     *     R     G(12)G
     * </pre>
     */
    /*
     * Erstellt eine Welt aus einer als Text codierten Karte.
     */
    public void weltAusKarteErstellen( String map ) {
        String[] lines = map.trim().split("\n");

        int y = 0;
        for( String line : lines ) {
            line = line.trim();
            if( y >= getHeight() || line.startsWith("//") || line.isEmpty() ) {
                continue;
            }

            parseLine(y, line);
            y += 1;
        }
    }

    /**
     * �bersetzt eine Zeile einer Text-Karte in eine Reihe in der Welt.
     * @param pY Zeilennummer / Reihe in der Welt
     * @param pLine Text-Zeile der Karte
     */
    private void parseLine( int pY, String pLine ) {
        pLine = pLine.trim();
        if( pY >= getHeight() || pLine.startsWith("//") || pLine.isEmpty() ) {
            return;
        }

        char[] chars = pLine.toLowerCase().toCharArray();

        Random r = new Random();
        int x = 0;
        for( int i = 0; i < chars.length; i++ ) {
            // Skip spaces
            if( chars[i] == ' ' ) {
                continue;
            }
            // Stop after exceeding the world bounds
            if( x >= getWidth() ) {
                break;
            }

            // Random cases first
            // replace current char with result
            String choices;
            switch( chars[i] ) {
                case 'z':
                    choices = " hmg";
                    chars[i] = choices.charAt(r.nextInt(choices.length()));
                    break;

                case '[':
                    // parse choices
                    int j = i;
                    choices = "";
                    while( i + 1 < chars.length && chars[i + 1] != ']' ) {
                        choices += chars[++i];
                    }
                    if( i + 1 < chars.length ) {
                        i += 1;
                    }
                    chars[i] = choices.charAt(r.nextInt(choices.length()));
                    break;
            }

            // Create object(s)
            if( chars[i] == '(' ) {
                while( i + 1 < chars.length && chars[i + 1] != ')' ) {
                    createObject(x, pY, chars[++i]);
                }
                i += 1;
            } else {
                createObject(x, pY, chars[i]);
            }
            x += 1;
        }
    }

    /**
     * Erstellt ein Actor-Objekt in der Welt.
     * @param pX x-Position
     * @param pY y-Position
     * @param pType Typ des Actors
     * @see #weltAusKarteErstellen(String)
     */
    private void createObject( int pX, int pY, char pType ) {
        if( pX < 0 || pX >= getWidth() || pY < 0 || pY >= getHeight() ) {
            return;
        }

        switch( pType ) {
            case 'r':
            case '>':
                addObject(new Rover(), pX, pY);
                break;

            case '^':
                Rover r1 = new Rover();
                addObject(r1, pX, pY);
                r1.setRotation(270);
                break;

            case 'v':
                Rover r2 = new Rover();
                addObject(r2, pX, pY);
                r2.setRotation(90);
                break;

            case '<':
                Rover r3 = new Rover();
                addObject(r3, pX, pY);
                r3.setRotation(180);
                break;

            case 'm':
                addObject(new Marke(), pX, pY);
                break;

            case 'h':
                addObject(new Huegel(), pX, pY);
                break;

            case 'g':
                addObject(new Gestein(), pX, pY);
                break;
        }
    }

    /**
     * L�dt eine Karte aus einer Datei im maps-Ordner.
     * @param pFile Dateiname einer Datei im maps-Ordner
     */
    public void karteLaden( String pFile ) {
        if( pFile.indexOf(".") < 0 ) {
            pFile += ".map";
        }

        String map = "";
        try {
            InputStream i = Planet.class.getResourceAsStream("maps/" + pFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(i));
            String st;
            while( (st = br.readLine()) != null ) {
                map += st + "\n";
            }
            i.close();
        } catch( Exception e ) {
            System.err.println("Karte " + pFile + " konnte nicht geladen werden.");
        }

        weltLeeren();
        weltAusKarteErstellen(map);
    }

    /**
     * Speichert die aktuelle Welt als Karte im maps-Ordner.
     * <p>
     * Wenn <var>pFile</var> <code>null</code> oder ein leerer String ist,
     * dann wird die Karte in der Konsole ausgegeben anstatt in einer Datei.
     * @param pFile Name der Karte.
     */
    public void karteSpeichern( String pFile ) {
        if( pFile == null ) {
            pFile = "";
        }

        String map = "";
        int w = getWidth(), h = getHeight();
        for( int i = 0; i < getHeight(); i++ ) {
            for( int j = 0; j < getWidth(); j++ ) {
                java.util.List<Actor> actors = getObjectsAt(j, i, Actor.class);
                actors.removeIf(new Predicate<Actor>() {
                    @Override
                    public boolean test( Actor actor ) {
                        return !(actor.getClass().equals(Huegel.class) ||
                            actor.getClass().equals(Gestein.class) ||
                            actor.getClass().equals(Marke.class) ||
                            actor.getClass().equals(Rover.class));
                    }
                });
                if( actors.size() == 0 ) {
                    map += '.';
                } else {
                    if( actors.size() > 1 ) {
                        map += '(';
                    }
                    java.util.Iterator<Actor> it = actors.iterator();
                    while( it.hasNext() ) {
                        Actor actor = it.next();
                        if( actor.getClass().equals(Huegel.class) ) {
                            map += 'H';
                        } else if( actor.getClass().equals(Gestein.class) ) {
                            map += 'G';
                        } else if( actor.getClass().equals(Marke.class) ) {
                            map += 'M';
                        } else if( actor.getClass().equals(Rover.class) ) {
                            Rover rover = (Rover) actor;
                            switch( rover.getRotation() ) {
                                case 0:
                                    map += 'R';
                                    break;
                                case 90:
                                    map += 'v';
                                    break;
                                case 180:
                                    map += '<';
                                    break;
                                case 270:
                                    map += '^';
                                    break;
                            }
                        }
                    }
                    if( actors.size() > 1 ) {
                        map += ')';
                    }
                }
            }
            map += '\n';
        }

        // Optimize map
        // Remove empty cells at end of lines
        map = Pattern.compile("\\.*(?=(\n|$))").matcher(map).replaceAll("");
        // Remove empty lines at and of map
        map = Pattern.compile("\\n+$").matcher(map).replaceAll("");
        // Add one empty cell to empty lines at start of map
        map = Pattern.compile("\\n(?=(\n|$))").matcher(map).replaceAll("\n.");
        map = Pattern.compile("^\\n").matcher(map).replaceAll(".\n");

        if( pFile.length() == 0 ) {
            // Replace newline with "\n" (as string) for printing
            map = Pattern.compile("\\n").matcher(map).replaceAll("\\\\n");
            System.out.println(map);
        } else {
            // Create maps file
            if( pFile.indexOf(".") < 0 ) {
                pFile += ".map";
            }
            try {
                String maps_path = Planet.class
                    .getResource("maps/").getPath();
                PrintWriter i = new PrintWriter(
                    new File(maps_path + pFile));
                i.print(map);
                i.flush();
                i.close();
            } catch( Exception e ) {
                System.err.println("Karte " + pFile + " konnte nicht gespeichert werden.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt eine prozedual generierte Zufallskarte.
     * @see #zufallswelt(long)
     */
    public void zufallswelt() {
        Random r = new Random();
        zufallswelt(r.nextLong());
    }

    /**
     * Erstellt eine prozedual generierte  Pseudo-Zufallskarte
     * <p>
     * Die generierte Karte wird abh�nig vom <var>seed</var>
     * generiert. Dabei wird nicht rein zuf�llig entschieden,
     * welche Objekte wo platziert werden, sondern es wird versucht
     * zusammenh�ngende H�gelketten zu erstellen.
     * <p>
     * Bei gleichen Werten f�r <var>seed</var> werden gleiche
     * Karten erstellt. Die Objekte sind zuf�llig verteilt, es
     * kann aber eine Karte reproduziert werden, wenn derselbe
     * <var>seed</var> wiederverwendet wird.
     * <p>
     * Der aktuelle <var>seed</var> wird unten rechts angezeigt.
     * @param seed
     */
    public void zufallswelt( long seed ) {
        String map = "";

        // Generate random feature points
        int[][] points = new int[6][2];
        int maxX = getWidth(), maxY = getHeight();

        Random r = new Random(seed);
        for( int i = 0; i < points.length; i++ ) {
            points[i] = new int[2];
            points[i][0] = r.nextInt(maxX);
            points[i][1] = r.nextInt(maxY);
        }

        // Generate Noise
        int[][] dist = new int[maxX][maxY];
        for( int y = 0; y < maxY; y++ ) {
            for( int x = 0; x < maxX; x++ ) {
                double d1 = Integer.MAX_VALUE;
                for( int i = 0; i < points.length; i++ ) {
                    double d2 = Math.sqrt(Math.pow(points[i][0] - x, 2.0) + Math.pow(points[i][1] - y, 2.0));
                    if( d2 < d1 ) {
                        d1 = d2;
                    }
                }
                dist[x][y] = (int) Math.round(d1);
            }
        }

        // Generate map
        boolean roverSet = false;
        char nextChar = '#';
        for( int y = 0; y < maxY; y++ ) {
            for( int x = 0; x < maxX; x++ ) {
                if( dist[x][y] > 3 ) {
                    int rand = r.nextInt(100);
                    if( rand <= 35 ) {
                        nextChar = 'g';
                    } else if( rand <= 40 ) {
                        nextChar = 'm';
                    } else {
                        nextChar = '#';
                    }
                    if( !roverSet && rand <= 15 ) {
                        char[] positions = new char[]{'>', '<', '^', 'v'};
                        nextChar = positions[r.nextInt(4)];
                        roverSet = true;
                    }
                } else if( dist[x][y] < 2 ) {
                    nextChar = 'h';
                } else if( dist[x][y] == 2 ) {
                    if( r.nextInt(100) <= 60 ) {
                        nextChar = 'h';
                    } else {
                        nextChar = '#';
                    }
                } else {
                    nextChar = '#';
                }

                map += nextChar;
            }
            map += "\n";
        }

        // Display map
        weltLeeren();
        weltAusKarteErstellen(map);

        // Show seed on map
        showText(seed + "", 12, 11);

        // Mark feature points on map
        //for( int i = 0; i < points.length; i++ ) {
        //    addObject(new Marke(), points[i][0], points[i][1]);
        //}
    }

    /**
     * Erstellt eine komplett zuf�llige Karte
     * <p>
     * Alle Objekte werden rein zuf�llig platziert und
     * die Welt rundherum mit einer "H�gelwand" umrandet.
     * Daher kann die entstehende Karte unter Umst�nden
     * schlecht nutzbar sein.
     */
    public void zufallswelt2() {
        String map = "";
        for( int y = 0; y < getHeight(); y++ ) {
            for( int x = 0; x < getWidth(); x++ ) {
                if( x == 0 || y == 0 || x == getWidth() - 1 || y == getHeight() - 1 ) {
                    map += "h";
                } else {
                    Random r = new Random();
                    int i = r.nextInt(10);
                    if( i < 5 ) {
                        map += "#";
                    } else if( i < 8 ) {
                        map += "h";
                    } else {
                        map += "g";
                    }
                }
            }
            map += "\n";
        }
        weltLeeren();
        weltAusKarteErstellen(map);
    }


    /**
     * Erstellt eine Welt, die einen Weg enth�lt
     */
    public void zufallsweg() {
        int maxX = getWidth(), maxY = getHeight();
        int halfX = (int) (maxX / 2), halfY = (int) (maxY / 2);

        char[][] map = new char[maxX][maxY];
        for( int y = 0; y < maxY; y++ ) {
            for( int x = 0; x < maxX; x++ ) {
                map[x][y] = 'H';
            }
        }

        Random r = new Random();
        // Generate random center points
        int[] p1 = new int[2], p2 = new int[2];
        // left half
        p1[0] = r.nextInt(halfX - 6) + 3;
        p1[1] = r.nextInt(maxY - 8) + 4;
        p2[0] = r.nextInt(halfX - 6) + 3 + halfX;
        p2[1] = r.nextInt(maxY - 8) + 4;

        // Generate random edge points

        // Generate path
        //int startX = p1[0]+3, startY = p1[1];

        map[p1[0]][p1[1]] = 'M';
        map[p2[0]][p2[1]] = 'M';

        while( p1[0] != p2[0] || p1[1] != p2[1] ) {
            int q = r.nextInt(2);
            if( p1[q] != p2[q] ) {
                p1[q] -= Math.signum(p1[q] - p2[q]);
                map[p1[0]][p1[1]] = '#';
            }
        }

        // print(map);

        String m = "";
        for( int y = 0; y < maxY; y++ ) {
            for( int x = 0; x < maxX; x++ ) {
                m += map[x][y];
            }
            m += "\n";
        }
        // Display map
        weltLeeren();
        weltAusKarteErstellen(m);
    }

    private boolean finished( int[][] arr ) {
        for( int y = 0; y < arr[0].length; y++ ) {
            for( int x = 0; x < arr.length; x++ ) {
                if( arr[x][y] != 2 && arr[x][y] != 0 ) {
                    return false;
                }
            }
        }
        return true;
    }

    private void print( int[][] arr ) {
        for( int y = 0; y < arr[0].length; y++ ) {
            for( int x = 0; x < arr.length; x++ ) {
                System.out.print(arr[x][y]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void print( char[][] arr ) {
        for( int y = 0; y < arr[0].length; y++ ) {
            for( int x = 0; x < arr.length; x++ ) {
                System.out.print(arr[x][y]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}