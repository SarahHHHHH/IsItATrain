/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yanisoukaci
 */
package TrainSimulator;
import java.io.*;
import java.util.*;
public class TrackModel
{
    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<TrackController> lineControllers = new ArrayList<TrackController>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    HashMap<TrackController,int[]> controllers = new HashMap<>();
    ArrayList<TrackController> cont_list = new ArrayList<>();
    String trackID;
    String block_line;
    PhantomCTCGUI ctc;
    public TrackModel(String filename, PhantomCTCGUI ctcIn)
    {
        try
        {
            BufferedReader initializer = new BufferedReader(new FileReader(filename));
            ctc = ctcIn;
            
            trackID = initializer.readLine();
            
            while(initializer.ready())
            {
                block_line = initializer.readLine();
                String splitter[] = block_line.split(" ");
                double data[] = new double[splitter.length];
                for(int i = 0; i < splitter.length; i++)
                {
                    data[i] = Double.parseDouble(splitter[i]);
                }
                Block b = new Block(data);
                blocks.add(b);
            }
            
            this.ctc.setNumOfBlocks(this.blocks.size());
            //Train is on the first block in the arraylist -> in yard -> block Id 0!!
            blocks.get(0).setTrain(true);
            initializer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        int counter = 1;
        
        //fix this shit
        for(int i = 0; i < blocks.size(); i = i + 25)
        {
            int start_block = i;
            int end_block = start_block + 24;
            if(i + 24 >= blocks.size())
            {
                end_block = blocks.size();
            }
            TrackController c = new TrackController(counter, start_block, end_block, this, ctc);
            this.lineControllers.add(c);
            int[] a = {start_block,end_block};
            controllers.put(c, a);
            cont_list.add(c);
        }
    }
    
    public ArrayList<TrackController> getLineControllers(){
        return (this.lineControllers);
    }
    
    public void trackSwitch(int id, int pos)
    {
        for(Switch s : switches)
        {
            if(s.block_id[0] == id)
            {
                s.activateSwitch(pos);
            }
        }
    }
    //for train model
    public double getSpeedLimit(int block_num)
    {
        Block b = blocks.get(block_num);
        return b.speedLimit();
    }
    //for train model, from track controller
    public double getSpeed(int block_num, int train_id)
    {
        TrackController temp =cont_list.get(0);
        for (TrackController c : controllers.keySet())
        {
            int[] block_range = controllers.get(c);
            
            if(block_num >= block_range[0] && block_num <= block_range[1])
            {
                temp = c;
                break;
            }
        }
        return temp.getSpeed(train_id, block_num);
    }
    //for train model, from track controller
    public double getAuthority(int block_num, int train_id)
    {
        TrackController temp = cont_list.get(0);
        for (TrackController c : controllers.keySet())
        {
            int[] block_range = controllers.get(c);
            
            if(block_num >= block_range[0] && block_num <= block_range[1])
            {
                temp = c;
                break;
            }
        }
        return temp.getAuthority(train_id);
    }
    
    public int getStartingBlock()
    {
        ctc.setCurrBlock(0);
        return 0;
    }
    public String getBeacon(int block_num, int train_id)
    {
        Block b = blocks.get(block_num);
        return b.setBeacon();
    }
    public double getGrade(int block_num)
    {
        return blocks.get(block_num).grade();
    }
    public double getBlockLength(int block_num)
    {
        return blocks.get(block_num).size();
    }
    public int getNextBlock(int curr, int prev)
    {
        int next = curr + 1;
        if (next >= blocks.size())
        {
            next = 0;
        }
        
        blocks.get(curr).setTrain(false);
        blocks.get(next).setTrain(true);
        if(blocks.get(next).crossingOnBlock())
        {
            ctc.setIsCrossBar("Yes");
        }
        else
        {
            ctc.setIsCrossBar("No");
        }
        if (next >= blocks.size()-1){
            //don't check for crossing
        }
        //check if there is a crossing on the next block
        else{
            if(blocks.get(next+1).crossingOnBlock())
            {
                TrackController temp = null;
                for (TrackController c : controllers.keySet())
                {
                    int[] block_range = controllers.get(c);

                    if(next >= block_range[0] && next <= block_range[1])
                    {
                        temp = c;
                        break;
                    }
                }
                ctc.setCrossBarStatus("Dropped Crossbar");
                temp.dropCrossBar(next + 1, this);
            }
        }
        if(blocks.get(curr).crossingOnBlock())
        {
            TrackController temp = null;
            for (TrackController c : controllers.keySet())
            {
                int[] block_range = controllers.get(c);

                if(next >= block_range[0] && next <= block_range[1])
                {
                    temp = c;
                    break;
                }
            }
            ctc.setCrossBarStatus("Raised Crossbar");
            temp.raiseCrossBar(curr, this);
        }
        
        ctc.setCurrBlock(next);
        return next;
    }
    public int getTrainBlockID()
    {
        int curr = 0;
        for(Block b : blocks)
        {
            if(b.trainOnBlock())
            {
                curr = b.getBlockID();
                break;
            }
        }
        
        return curr;
    }
}
