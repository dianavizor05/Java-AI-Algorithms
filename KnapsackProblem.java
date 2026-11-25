import java.io.*;
import java.util.*;

public class KnapsackProblem {

    static class Przedmiot {
        String nazwa;
        int waga;
        int wartosc;

        Przedmiot(String nazwa, int waga, int wartosc) {
            this.nazwa = nazwa;
            this.waga = waga;
            this.wartosc = wartosc;
        }

        double stosunek() {
            return (double) wartosc / waga;
        }
    }

    static class Wynik {
        ArrayList<Przedmiot> przedmioty;
        int sumaWag;
        int sumaWartosci;

        Wynik(ArrayList<Przedmiot> przedmioty, int sumaWag, int sumaWartosci) {
            this.przedmioty = przedmioty;
            this.sumaWag = sumaWag;
            this.sumaWartosci = sumaWartosci;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Podaj nazwe pliku: ");
        String nazwaPliku = scanner.nextLine();

        System.out.print("Podaj pojemnosc plecaka: ");
        int pojemnosc = scanner.nextInt();

        ArrayList<Przedmiot> przedmioty;

        try {
            przedmioty = wczytajPrzedmioty(nazwaPliku);
        } catch (Exception e) {
            System.out.println("Nie udalo sie wczytac pliku.");
            return;
        }

        long startSilowy = System.nanoTime();
        Wynik wynikSilowy = metodaSilowa(przedmioty, pojemnosc);
        long koniecSilowy = System.nanoTime();

        long startZachlanny = System.nanoTime();
        Wynik wynikZachlanny = metodaZachlanna(przedmioty, pojemnosc);
        long koniecZachlanny = System.nanoTime();

        System.out.println("\n--- PODEJSCIE SILOWE ---");
        wypiszWynik(wynikSilowy);
        System.out.println("Czas wykonania: " + (koniecSilowy - startSilowy) + " ns");

        System.out.println("\n--- PODEJSCIE ZACHLANNE ---");
        wypiszWynik(wynikZachlanny);
        System.out.println("Czas wykonania: " + (koniecZachlanny - startZachlanny) + " ns");
    }

    static ArrayList<Przedmiot> wczytajPrzedmioty(String nazwaPliku) throws IOException {
        ArrayList<Przedmiot> lista = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(nazwaPliku));
        String linia;

        while ((linia = br.readLine()) != null) {
            String[] dane = linia.split(";");

            String nazwa = dane[0];
            int waga = Integer.parseInt(dane[1]);
            int wartosc = Integer.parseInt(dane[2]);

            lista.add(new Przedmiot(nazwa, waga, wartosc));
        }

        br.close();
        return lista;
    }

    static Wynik metodaSilowa(ArrayList<Przedmiot> przedmioty, int pojemnosc) {
        int n = przedmioty.size();
        int liczbaKombinacji = 1 << n;

        int najlepszaWartosc = 0;
        int najlepszaWaga = 0;
        ArrayList<Przedmiot> najlepszyZestaw = new ArrayList<>();

        for (int maska = 0; maska < liczbaKombinacji; maska++) {
            int sumaWag = 0;
            int sumaWartosci = 0;
            ArrayList<Przedmiot> aktualnyZestaw = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((maska & (1 << i)) != 0) {
                    Przedmiot p = przedmioty.get(i);
                    sumaWag += p.waga;
                    sumaWartosci += p.wartosc;
                    aktualnyZestaw.add(p);
                }
            }

            if (sumaWag <= pojemnosc && sumaWartosci > najlepszaWartosc) {
                najlepszaWartosc = sumaWartosci;
                najlepszaWaga = sumaWag;
                najlepszyZestaw = aktualnyZestaw;
            }
        }

        return new Wynik(najlepszyZestaw, najlepszaWaga, najlepszaWartosc);
    }

    static Wynik metodaZachlanna(ArrayList<Przedmiot> przedmioty, int pojemnosc) {
        ArrayList<Przedmiot> posortowane = new ArrayList<>(przedmioty);

        posortowane.sort(new Comparator<Przedmiot>() {
            public int compare(Przedmiot a, Przedmiot b) {
                return Double.compare(b.stosunek(), a.stosunek());
            }
        });

        ArrayList<Przedmiot> wybrane = new ArrayList<>();
        int sumaWag = 0;
        int sumaWartosci = 0;

        for (Przedmiot p : posortowane) {
            if (sumaWag + p.waga <= pojemnosc) {
                wybrane.add(p);
                sumaWag += p.waga;
                sumaWartosci += p.wartosc;
            }
        }

        return new Wynik(wybrane, sumaWag, sumaWartosci);
    }

    static void wypiszWynik(Wynik wynik) {
        System.out.println("Wybrane przedmioty:");

        for (Przedmiot p : wynik.przedmioty) {
            System.out.println(p.nazwa + "; waga: " + p.waga + "; wartosc: " + p.wartosc);
        }

        System.out.println("Laczna waga: " + wynik.sumaWag);
        System.out.println("Laczna wartosc: " + wynik.sumaWartosci);
    }
}