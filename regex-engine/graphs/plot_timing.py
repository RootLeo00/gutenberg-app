import pandas as pd
import matplotlib.pyplot as plt

# plt.style.use('style.mplstyle')

def plot_timing(file, x, y):
    
    listdf=[]
    name=[]
    fig=plt.figure(figsize=(9, 9))
    plt.subplots_adjust(left=0.25, bottom=0.15)
    print("plotting: ", file)
    i=0
    for f in file:
        listdf.append(pd.read_pickle(f))
        print(listdf[i])

        #retrieve engine name and pattern
        tmp=f.split("/")[-1]
        name.append(tmp.split("_")[1])
        print("name", name)

        #plot xtick every 10 elements 
        df=listdf[i]
        plt.scatter(df[x][::5], df[y][::5], label=name[i].upper(), marker='o')
        plt.plot(df[x], df[y])
        i+=1

    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)

    plt.legend()
    plt.xlabel(x, fontsize=12)
    plt.ylabel(y, fontsize=12)

    plt.savefig('./'+x+'_'+y+'.png')
    plt.show()


import sys
if __name__ == "__main__":
    # main()
	# filename=sys.argv[1]
	# pattern=sys.argv[2]
    file=["../output/output_radixtree_patternlength.pkl","../output/output_kmp_patternlength.pkl"]
    plot_timing(file,  "pattern_len","time_elapsed")
    file=["../output/output_radixtree_textlength.pkl","../output/output_kmp_textlength.pkl"]
    plot_timing( file, "ncharacters", "time_elapsed")