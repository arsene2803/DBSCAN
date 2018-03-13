package reducer;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.Configuration.type;

public class RelabelReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// process values
		type ptype=type.NOISE;
		String clusterId=null;
		for (Text val : values) {
			String[] line=val.toString().split("|");
			type flag=type.valueOf(line[0]);
			String id=line[1];
			if(clusterId==null)
				clusterId=id;
			if(flag==type.BORDER)
				ptype=type.BORDER;
			else if(flag==type.CORE)
				ptype=type.CORE;

		}
		context.write(_key,new Text(ptype+"|"+clusterId));
	}

}
