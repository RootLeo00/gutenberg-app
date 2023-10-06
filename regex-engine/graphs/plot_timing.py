import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
def plot_timing(file, x, y):
    #read the pickle file (file)
    df = pd.read_pickle(file)
    #plot with matplot lib a lineplot
    sns.set(style="darkgrid")
    # Plot the responses for different events and regions
    
    sns.lineplot(x=x, y=y, data=df)
    #show
    plt.show()


import sys
if __name__ == "__main__":
    # main()
	# filename=sys.argv[1]
	# pattern=sys.argv[2]
	plot_timing("../output/output_kmp.pkl", "time_elapsed", "ncharacters")
	# plot_timing("../output/output_kmp.pkl",  "time_elapsed","pattern_len")