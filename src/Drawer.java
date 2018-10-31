import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Drawer {
    Network n;
    NetworkDisplay nd;
    public Drawer (Network n, NetworkDisplay nd){
        this.n = n;
        this.nd = nd;

        nd.addButton ("new", (ActionEvent e) -> {
            Thread t = new Thread (()-> NetworkDisplay.main (new String []{}));
            t.start ();
        });
        nd.addButton ("close", (ActionEvent e) ->{
            nd.setVisible(false);
            nd.stop = true;
            nd.dispose ();
        });
        nd.addButton ("new training batch", (ActionEvent e)-> n.setQAArray());
        nd.addButton ("train one batch",(ActionEvent e) -> n.runBatch());
        nd.addButton (">/II slow training", (ActionEvent e) -> {
            if (nd.stop) {
                nd.stop = false;
                Thread tempThread = new Thread(() ->{
                    long time = System.currentTimeMillis();
                    while (!nd.stop) {
                        //n.runBatch();
                        //try {
                        //    Thread.sleep(30);
                        //}
                        //catch (Exception f){
                        //    System.out.println ("sleeping failed");
                        //}

                        try {
                            Thread.sleep((int)(Math.max (-(System.currentTimeMillis () - time - 30), 0)));
                        } catch (Exception g) {
                            System.out.println("um");
                        }
                        time = System.currentTimeMillis();
                        n.runBatch();
                    }
                });
                tempThread.start();

            } else
                nd.stop = true;
        });
        nd.addButton (">/II fast training",(ActionEvent e) ->{
            if (nd.stop){
                nd.stop = false;
                Thread tempThread = new Thread (() ->{
                    while (!nd.stop){
                        n.runBatch();
                    }
                });
                tempThread.start();
            }
            else
                nd.stop = true;
        });
        nd.addButton ("lr * 2", (ActionEvent e) -> n.lr *= 2.0);
        nd.addButton ("lr / 2", (ActionEvent e) -> n.lr /= 2.0);
        nd.addButton ("update network", (ActionEvent e) -> updateNetwork = true);

        halfred = new Color(255, 0, 0, 122);
        halfblue = new Color (0, 0, 255, 122);
        halfwhite = new Color (255, 255, 255, 122);
    }

    int windowWidth;
    int windowHeight;

    Color halfred;
    Color halfblue;
    Color halfwhite;

    private ArrayList <ArrayList <Neuron>> layers = new ArrayList<> ();
    private ArrayList <ArrayList <XY>> positions = new ArrayList<>();
    public void draw (){
        try {
            ArrayList <String> displayText = new ArrayList<>();
            int neuronRadius = nd.neuronRadius;
            int fontSize = nd.fontSize;
            nd.setFont (fontSize);

            int errorGraphWidth = windowWidth * 7 / 10;

            double max = 0;
            double min = Double.MAX_VALUE / 2.0;
            int left = windowWidth - errorGraphWidth;
            try {
                for (int x = 0; x < n.errors.size(); x++) {
                    if (n.errors.get(x) > max)
                        max = n.errors.get(x);
                    if (n.errors.get(x) < min)
                        min = n.errors.get(x);
                }

                nd.setColor(Color.BLACK);

                for (int x = 0; x < n.errors.size(); x++) {

                    //ignore some points if there are more points than "space" to display them

                    int xPos = left + (int) (1.0 * x * errorGraphWidth / n.errors.size());
                    try {
                        int yPos = (int) (((max - min) - (n.errors.get(x) - min)) * 100 / (max - min));
                        nd.drawRect(xPos, yPos, 1, 1);
                    } catch (Exception f) {
                    }
                }
            }
            catch (Exception e){}
            nd.drawLine(left - 1, 0, left - 1, 101);
            nd.drawLine(left - 1, 101, left + errorGraphWidth, 101);

            nd.drawString(max + "", left - 1, -1, "topright");
            nd.drawString ("error", left - 10, 50, "middleright");
            nd.drawString(min + "", left - 1, 101, "bottomright");
            nd.drawString((n.batchesRun > n.maxErrorRecording ? n.batchesRun - n.maxErrorRecording : 0)+"", left, 101, "topleft");
            nd.drawString ("batch number", left + errorGraphWidth/2, 111, "topmiddle");
            nd.drawString(n.batchesRun +"", left + errorGraphWidth, 101, "topright");

            //if (n.errors.size() > 0)
            //    displayText.add ("current errors: " + n.errors.get(Math.min (n.errors.size(), Network.maxErrorSize)-1)+"");

            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);

                    for (int z = 0; z < layers.get(x).get(y).inputs.size(); z++) {
                        XY neuronPos = getPosition(layers.get(x).get(y).inputs.get(z).n1);
                        XY inputNeuron = positions.get(neuronPos.x).get(neuronPos.y);

                        double val = layers.get(x).get(y).inputs.get(z).w;
                        if (val > 0) {
                            nd.setStroke (Math.sqrt(val));
                            nd.setColor(halfblue);
                        } else {
                            nd.setStroke (Math.sqrt (-val));
                            nd.setColor(halfred);
                        }
                        nd.drawLine(inputNeuron.x + neuronRadius, inputNeuron.y, thisNeuron.x - neuronRadius, thisNeuron.y);

                        nd.drawDouble(val, (thisNeuron.x + inputNeuron.x) / 2, (thisNeuron.y + inputNeuron.y) / 2, "middle");
                    }
                }
            }

            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);
                    Neuron neuron = layers.get(x).get(y);

                    double bias = neuron.bias;
                    if (bias > 0) {
                        nd.setStroke (bias);
                        nd.setColor(Color.BLUE);
                    } else {
                        nd.setStroke (-bias);
                        nd.setColor(Color.RED);
                    }
                    nd.drawOval(thisNeuron.x - neuronRadius,
                            thisNeuron.y - neuronRadius,
                            neuronRadius * 2, neuronRadius * 2);
                    nd.drawDouble(neuron.value, thisNeuron.x, thisNeuron.y, "middle");
                    nd.drawDouble(bias, thisNeuron.x, thisNeuron.y + neuronRadius + 10, "middle");
                }

            }

            for (int x = 0; x < n.inputs.size(); x++) {
                XY pos0 = getPosition(n.inputs.get(x));
                XY pos = positions.get(pos0.x).get (pos0.y);
                nd.drawString("in " + x, pos.x - neuronRadius * 2, pos.y, "middle");
            }
            for (int x = 0; x < n.outputs.size(); x++) {
                XY pos0 = getPosition(n.outputs.get(x));
                XY pos = positions.get(pos0.x).get(pos0.y);
                nd.drawString("out " + x, pos.x + neuronRadius * 2, pos.y, "middle");
            }



            //plot the data?

            int dataSquareSide = 100;

            left = windowWidth - dataSquareSide;
            int top = windowHeight/2 - dataSquareSide/2;

            ArrayList <double[]> lastBatch = n.thisBatchPoints;

            double [] maxes = new double [lastBatch.get(0).length];
            double [] mins = new double [lastBatch.get(0).length];

            Arrays.fill (maxes, Double.MIN_VALUE/2.0);
            Arrays.fill (mins, Double.MAX_VALUE/2.0);
//
            for (int x = 0; x < lastBatch.size(); x ++){
                for (int y = 0; y < lastBatch.get(0).length; y ++){
                    if (lastBatch.get (x) [y] > maxes [y])
                        maxes [y] = lastBatch.get(x)[y];
                    if (lastBatch.get(x)[y] < mins [y])
                        mins [y] = lastBatch.get(x)[y];
                }
            }
            displayText.add (maxes[0]+" " + mins[0]);
            displayText.add (maxes[1]+" " + mins[1]);
            displayText.add (maxes[2]+" " + mins[2]);

            if (lastBatch.get(0).length == 3 && n.inputs.size() == 2){

                nd.drawDouble (maxes [0], windowWidth, top + dataSquareSide,"topright");
                nd.drawDouble (mins[0], windowWidth - dataSquareSide, top + dataSquareSide, "topleft");

                nd.drawDouble (maxes[1], windowWidth - dataSquareSide, top, "topright");
                nd.drawDouble (mins [1], windowWidth - dataSquareSide, top + dataSquareSide, "bottomright");

                for (int x = 0; x < lastBatch.size(); x ++){
                    double [] thisCase = lastBatch.get(x);

                    int xPosition = (int)(left + dataSquareSide * ((thisCase [0] - mins [0])/(maxes [0] - mins [0])));
                    int yPosition = (int)(top + dataSquareSide - dataSquareSide * ((thisCase [1] - mins [1])/(maxes [1] - mins [1])));
                    int color = (int)(((thisCase [2] - mins [2])/(maxes [2] - mins [2])) * 255.0 * 2.0 - 255.0);

                    //if (x == 0){
                    //    displayText.add (xPosition + ", " + yPosition + " - " + color);
                    //}

                    if (color > 255)
                        color = 255;
                    if (color < -255)
                        color = -255;

                    if (color < 0) {
                        color = -color;
                        nd.setColor(new Color(color, 0, 0));
                    } else
                        nd.setColor(new Color(0, 0, color));

                    nd.drawRect (xPosition, yPosition, 1, 1);

                }
            }

            displayText.add (nd.keyboard);
            displayText.add ("lr: " + n.lr);

            nd.setColor(Color.BLACK);
            for (int x = 0; x < displayText.size(); x++)
                nd.drawString(displayText.get(x), 0, fontSize * (x + 1), "bottomleft");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    boolean updateNetwork = true;
    private boolean working = false;
    public void updateNetwork (){
        if (updateNetwork && !working){
            updateNetwork = false;
            working = true;
            ArrayList<Neuron> neurons = n.neurons;
            layers = new ArrayList<ArrayList<Neuron>> ();
            layers.add (new ArrayList <Neuron>());
            for (int x = 0; x < neurons.size(); x ++){
                boolean incrementLayer = false;
                for (int y = 0; y < neurons.get(x).inputs.size(); y ++){
                    for (int z = 0; z < layers.get (layers.size()-1).size(); z ++) {
                        if (neurons.get(x).inputs.get(y).n1 == layers.get(layers.size()-1).get (z))
                            layers.add (new ArrayList <Neuron>());
                    }
                }
                layers.get(layers.size()-1).add (neurons.get(x));
            }

            int width = layers.size();

            positions = new ArrayList<>();
            for (int x = 0; x < layers.size(); x ++){
                int nneurons = layers.get(x).size();
                positions.add (new ArrayList<>());
                for (int y = 0; y < nneurons; y ++){
                    positions.get(x).add (new XY((int)(windowWidth*(x+0.5)/(width+1)), windowHeight*(y+2)/(nneurons+3)));

                }

            }

            nd.setButtonPositions();

            working = false;
        }
    }

    private XY getPosition (Neuron n){
        for (int x = 0; x < layers.size(); x ++){
            for (int y = 0; y < layers.get(x).size(); y ++){
                if (layers.get(x).get(y) == n)
                    return new XY (x, y);
            }
        }
        System.out.println ("neuron not found");
        return new XY (-1, -1);
    }

}
