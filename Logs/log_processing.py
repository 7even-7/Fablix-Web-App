if __name__ == '__main__':
    f = open("TSLog.txt", "r")
    line = f.readline()
    TS_nums = []
    while line:
        TS_nums.append(int(line.split()[-1]))
        line = f.readline()
    f = open("TJLog.txt", "r")
    line = f.readline()
    TJ_nums = []
    while line:
        TJ_nums.append(int(line.split()[-1]))
        line = f.readline()
    f = open("TQLog.txt", "r")
    line = f.readline()
    TQ_nums = []
    while line:
        TQ_nums.append(int(line.split()[-1]))
        line = f.readline()
    f = open("AverageTimes.txt", "w")
    f.write(str(sum(TS_nums) / len(TS_nums)) + "\n")
    f.write(str(sum(TJ_nums) / len(TJ_nums)) + "\n")
    f.write(str(sum(TQ_nums) / len(TQ_nums)) + "\n")
    f.close()
    
