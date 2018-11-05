import java.util.ArrayList;

class QA {
    double[] realInputs;
    double[] realOutputs;

    static String [] allCases;
    static int count = 0;
    QA (){
        if (count > allCases.length){
            count = 0;
            System.out.println ("case size and batch size not matchin");
        }

        String [] temp = allCases[count].split (" ");
        int inputSize = 0;
        for (int x = 0; x < temp.length; x ++){
            if (temp [x].equals ("->"))
                inputSize = x;
        }
        int outputStart = inputSize + 1;
        int outputEnd = temp.length;
        realInputs = new double [inputSize];
        realOutputs = new double [outputEnd - outputStart];
        for (int x = 0; x < inputSize; x ++)
            realInputs [x] = Double.parseDouble(temp [x]);
        for (int y = outputStart; y < outputEnd; y ++)
            realOutputs [y - outputStart] = Double.parseDouble (temp [y]);
        count ++;
    }


    QA (String oneCase){
        //split and set
    }

    QA(int test) {
        try {
            double temp1;
            double temp2;
            double temp3;
            switch (test) {
                case 1:
                    addOutput(1 - get(0));
                    break;
                case 2:
                    addOutput(get(0) > 0.5 ? 1 : 0);
                    addOutput(get(1) > 0.5 ? 1 : 0);
                    break;
                case 3:
                    addOutput((get(0) > 0.5 ? 1 : 0) == (get(1) > 0.5 ? 1 : 0) ? 0 : 1);
                    break;
                case 4:
                    //get (0) = get (0) * 50.0 - 25.0;//range of x
                    //get (1) = get (1) * 50.0 - 25.0;//range of y
                    double x = get(0) * 50.0 - 25.0;
                    addOutput(get(1) * 50.0 - 25.0 > x * x ? 1 : 0);
                    break;
                case 5:
                    //get (0) = get (0) * 5.0 - 1.0;//range of x
                    //get (1) = get (1) * 5.0 - 2.5;//range of y
                    double y = get(0) * 5.0 - 1.0;
                    addOutput((get(1) * 5.0 - 2.5 > Math.log(y)) ? 1 : 0);
                    break;
                case 6:
                    //get (0) = get (0) * 50.0 - 25.0;//range of x
                    //get (1) = get (1) * 50.0 - 25.0;//range of y
                    double z = get(0) * 50.0 - 25.0;
                    addOutput((get(1) * 50.0 - 25.0 > 2.0 * z) ? 1 : 0);
                    break;
                case 7:
                    //get (0) = get (0) * 5.0 - 2.5;//range of x
                    //get (1) = get (1) * 5.0 - 2.5;//range of y
                    double a = get(0) * 5.0 - 2.5;
                    addOutput((get(1) * 5.0 - 2.5 > a * a * a) ? 1 : 0);
                    break;
                case 8:
                    //get (0) = get (0) * 5.0 - 2.5;//range of x
                    //get (1) = get (1) * 5.0 - 2.5;//range of y
                    double b = get(0) * 5.0 - 2.5;
                    addOutput((get(1) * 5.0 - 2.5 > b * b * b * b * b) ? 1 : 0);
                    break;
                case 9:
                    //get (0) = get (0) * 10.0 - 5.0;//range of x
                    //get (1) = get (1) * 10.0 - 5.0;//range of y
                    double c = get(0) * 10.0 - 5.0;
                    addOutput((get(1) * 10.0 - 5.0 > Math.exp(c)) ? 1 : 0);
                    break;
                case 10:
                    //get (0) = get (0) * 10.0 - 5.0;//range of x
                    //get (1) = get (1) * 10.0 - 5.0;//range of y
                    double d = get(0) * 10.0 - 5.0;
                    addOutput(((get(1) * 10.0 - 5.0 > 0) != (get(1) * 10.0 - 5.0 > d)) ? 1 : 0);
                    break;
                case 11:
                    double e = get(0) * 6.0 - 3.;
                    addOutput(((get(1) * 6.0 - 3.0 > 0) != (get(1) * 6.0 - 3.0 > e) != (get(1) * 6.0 - 3.0 > e * e - 2)) ? 1 : 0);
                    break;
                case 12:
                    double f = get(0) * 2.0 - 1.0;
                    addOutput(((get(1) * 2.0 - 1.0 > -0.3 != get(1) * 2.0 - 1.0 > +0.3 !=
                            get(1) * 2.0 - 1.0 > f - 0.3 != get(1) * 2.0 - 1.0 > f + 0.3)) ? 1 : 0);
                    break;
                case 13:
                    addOutput(((get(0) * 2.0 - 1.0) * (get(0) * 2.0 - 1.0) + (get(1) * 2.0 - 1.0) * (get(1) * 2.0 - 1.0) > 0.5) ? 1 : 0);
                    break;
                case 14:
                    addOutput(((get(0) * 2.0 - 1.0) * (get(0) * 2.0 - 1.0) + (get(1) * 2.0 - 1.0) * (get(1) * 2.0 - 1.0) > 0.8 !=
                            (get(0) * 2.0 - 1.0) * (get(0) * 2.0 - 1.0) + (get(1) * 2.0 - 1.0) * (get(1) * 2.0 - 1.0) > 0.3) ? 1 : 0);
                    break;
                case 15:
                    //get (0) = get (0) * 4.0 - 2.0;//range of x
                    //get (1) = get (1) * 4.0 - 2.0;//range of y
                    double g = get(0) * 4.0 - 2.0;
                    double h = get(1) * 4.0 - 2.0;
                    addOutput(((g + 1) * (g + 1) + (h) * (h) < 1 ||
                            ((g - 1) * (g - 1) + h * h < 1)
                    ) ? 1 : 0);
                    break;
                case 16:
                    double i = get(0);
                    double j = get(1);
                    addOutput((i > 0.25 && i < 0.75) ? (
                            (
                                    (j > 0.60 && j < 0.80 && i < 0.50)
                                            || (j > 0.20 && j < 0.40 && i > 0.50)
                            ) ? 0 : 1
                    ) : 0);
                    break;
                case 17:
                    get (1);//in the inequality, it may not be used, which results in an error maybe
                    addOutput((get(0) > 0.25 && get(0) < 0.75) ? (
                            (get(1) < 0.25 || get(1) > 0.75) ? 1 : 0
                    ) : 0);
                    break;
                case 18:
                    double k = get(0) * 100.0;
                    double l = get(1) * 100.0;
                    addOutput((Math.sqrt((k - 62.5) * (k - 62.5) + (l - 62.5) * (l - 62.5)) > 37.5 != Math.sqrt((k - 25.0) * (k - 25.0) + (l - 25.0) * (l - 25.0)) > 12.5) ? 1 : 0);
                    break;
                case 19:
                    addOutput(get(0));
                    addOutput(get(0) + get(1));
                    addOutput(get(0) * get(1));
                    addOutput(get(0) - get(1));
                    addOutput(Math.sqrt((get(0) - 0.5) * (get(0) - 0.5) + (get(1) - 0.5) * (get(1) - 0.5)) > 0.25 ? 1 : 0);
                    break;
                default://funky town
                    System.out.println("QA " + test + " not found.");
                    break;
            }


            realInputs = new double[inputs.size()];
            for (int x = 0; x < inputs.size(); x++)
                realInputs[x] = inputs.get(x);

            realOutputs = new double[outputs.size()];
            for (int x = 0; x < outputs.size(); x++)
                realOutputs[x] = outputs.get(x);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    static int maxQA = 19;
    private double [] [] inputRanges;
    private ArrayList <Double> outputs = new ArrayList<>();
    private void addOutput (double val){
        outputs.add (val);
    }

    ArrayList <Double> inputs = new ArrayList<>();
    private double get (int pos){
        if (pos >= inputs.size())
            for (int x = inputs.size(); x <= pos; x ++)
                inputs.add (Math.random());//0 to 1
        return inputs.get (pos);
    }
    
    /*void initialize(int size1, int size2) {
        realInputs = new double[size1];
        for (int x = 0; x < size1; x++) {
            realInputs[x] = Math.random();
        }
        realOutputs = new double[size2];
    }*/
    //void initialize (double [] [] ranges){
    //    inputRanges = ranges;
    //    realInputs = new double [ranges.length];
    //    for (int x = 0; x < realInputs.length; x ++){
    //        realInputs [x] = Math.random () * (ranges [x][1] - ranges[x][0]) + ranges[x][0];
    //    }
    //}
}
