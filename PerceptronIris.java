import java.io.*;
import java.util.*;

    public class PerceptronIris {

        static class Perceptron {
            double[] wagi;
            double prog;
            double alfa;

            public Perceptron(int ileWag, double prog, double alfa) {
                this.wagi = new double[ileWag];
                this.prog = prog;
                this.alfa = alfa;

                Random rand = new Random();

                for (int i = 0; i < ileWag; i++) {
                    wagi[i] = rand.nextDouble() - 0.5;
                }
            }

            public int classify(double[] wejscie){
                double suma = 0;

                for (int i = 0; i < wejscie.length; i++) { //iloczyn skalarny
                    suma += wejscie[i] * wagi[i];
                }

                if (suma >= prog) {
                    return 1; //iris-setosa
                } else {
                    return 0;//pozostałe
                }
            }

            public void learn(double[] wejscie, int poprawna){
                int wynik = classify(wejscie);

                //uczenie
                int blad = poprawna - wynik;

                for (int i = 0; i < wagi.length; i++){ //poprawianie wag
                    wagi[i] = wagi[i] + alfa * blad * wejscie[i];
                }

                prog = prog - alfa * blad; //poprawianie progu
            }
        }

        static List<double[]> wszystkieCechy = new ArrayList<>();
        static List<Integer> wszystkieKlasy = new ArrayList<>();

        public static void main(String[] args) throws Exception{
            Scanner sc = new Scanner(System.in);
            sc.useLocale(Locale.US); //0.1 nie 0,1

            BufferedReader br = new BufferedReader(new FileReader("iris.txt"));
            String linia;

            while ((linia = br.readLine()) != null) {
                String[] t = linia.split(",");

                if (t.length < 5) continue; //zabezpieczenie(lecimy dalej)

                double[] cechy = new double[4];

                for (int i = 0; i < 4; i++){ //zmiana na double
                    cechy[i] = Double.parseDouble(t[i]);
                }

                wszystkieCechy.add(cechy);

                if (t[4].equals("Iris-versicolor")){
                    wszystkieKlasy.add(1);
                } else {
                    wszystkieKlasy.add(0);
                }
            }

            br.close();

            List<double[]> treningCechy = new ArrayList<>();
            List<Integer> treningKlasy = new ArrayList<>();

            List<double[]> testCechy = new ArrayList<>();
            List<Integer> testKlasy = new ArrayList<>();

            //6 do treningu i 4 do testu
            for (int i = 0; i < wszystkieCechy.size(); i++){
                if (i % 10 < 6){
                    treningCechy.add(wszystkieCechy.get(i));
                    treningKlasy.add(wszystkieKlasy.get(i));
                } else {
                    testCechy.add(wszystkieCechy.get(i));
                    testKlasy.add(wszystkieKlasy.get(i));
                }
            }

            System.out.print("Podaj prog: ");
            double prog = sc.nextDouble();

            System.out.print("Podaj alfa: ");
            double alfa = sc.nextDouble();

            //ile raz przejść przez zbiór treningowy
            System.out.print("Podaj liczbe epok: ");
            int epoki = sc.nextInt();

            Perceptron p = new Perceptron(4, prog, alfa);

            for (int e = 0; e < epoki; e++) {
                for (int i = 0; i < treningCechy.size(); i++) {
                    p.learn(treningCechy.get(i), treningKlasy.get(i));
                }
            }

            //testowanie skutecznosci
            int dobre = 0;

            for (int i = 0; i < testCechy.size(); i++){
                int wynik = p.classify(testCechy.get(i));

                if (wynik == testKlasy.get(i)) {
                    dobre++;
                }
            }

            double celnosc = 100.0 * dobre / testCechy.size();

            System.out.println("Trening: " + treningCechy.size());
            System.out.println("Test: " + testCechy.size());
            System.out.println("Celnosc: " + celnosc + "%");

            System.out.println("Podaj 4 cechy kwiatu:");
            double[] nowy = new double[4];

            for (int i = 0; i < 4; i++) {
                nowy[i] = sc.nextDouble();
            }

            int wynik = p.classify(nowy);

            if (wynik == 1) {
                System.out.println("Wynik: Iris-versicolor");
            } else {
                System.out.println("Wynik: pozostale");
            }

            sc.close();
        }
    }
