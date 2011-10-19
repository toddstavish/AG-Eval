import os
import csv
import random
import getopt
import sys

class Data:
    dunsNbr = 1
    assnTypeCd = 2
    factualOwnerDunsNbr = 3
    publishedOwnerDunsNbr = 4
    globalUltimateDunsNbr = 6
    oprgStCd = 7
    hierCode = 13
    busName = 16
    trdgNme1 = 17
    trdgNme2 = 18
    physAdrStrAdrLine = 19
    physAdrPostTown = 20
    physAdrPostCode = 23
    wbCtry = 24
    physAdrStrAdrLine2 = 25
        
    
    def __init__(self):
        self.data = []
        for i in xrange(26):
            self.data.append(None)
            pass
        pass



class DataGenerator:
    def __init__(self,fileName,seed,dunsRange,probGlobal,probDifferentOwner):
        self.file = file(fileName,"w")
        self.csv  = csv.writer(self.file, delimiter='|')
        self.dunsRange = dunsRange
        self.probabilityDifferentOwner=probDifferentOwner
        self.probabilityGlobal=probGlobal
        random.seed(seed)
        pass


    def createData(self,
                   dunsNbr,assnTypeCd,factualOwnerDunsNbr,publishedOwnerDunsNbr,
                   globalUltimateDunsNbr,oprgStCd,hierCode,busName,
                   trdgNme1,trdgNme2,
                   physAdrStrAdrLine,physAdrPostTown,physAdrPostCode,wbCtry,physAdrStrAdrLine2):

        d = Data()
        d.data[Data.dunsNbr] = dunsNbr
        d.data[Data.assnTypeCd] = assnTypeCd
        d.data[Data.factualOwnerDunsNbr] = factualOwnerDunsNbr
        d.data[Data.publishedOwnerDunsNbr] = publishedOwnerDunsNbr
        d.data[Data.globalUltimateDunsNbr] = globalUltimateDunsNbr
        
        d.data[Data.oprgStCd] = oprgStCd
        d.data[Data.hierCode] = hierCode
        d.data[Data.busName] = busName
        d.data[Data.trdgNme1] = trdgNme1
        d.data[Data.trdgNme2] = trdgNme2
        d.data[Data.physAdrStrAdrLine] = physAdrStrAdrLine
        d.data[Data.physAdrPostTown] = physAdrPostTown
        d.data[Data.physAdrPostCode] = physAdrPostCode
        d.data[Data.wbCtry] = wbCtry
        d.data[Data.physAdrStrAdrLine2] = physAdrStrAdrLine2
        
        
        return d

    def writeHeader(self):
        header = self.createData("dunsNbr","assnTypeCd","factualOwnerDunsNbr","publishedOwnerDunsNbr",
                                 "globalUltimateDunsNbr","oprgStCd","hierCode","busName",
                                 "trdgNme1","trdgNme2",
                                 "physAdrStrAdrLine","physAdrPostTown","physAdrPostCode","wbCtry","physAdrStrAdrLine2")
        self.write(header)
        pass

    def write(self,data):
        self.csv.writerow(data.data)
        pass

    def generate(self,count):
        
        for i in xrange(count):
            publisher = random.randint(2e9,2e9+50)
            d = Data()
            d.data[Data.dunsNbr] = random.randint(1e9,1e9+self.dunsRange)
            d.data[Data.assnTypeCd] = "a"
            
            d.data[Data.publishedOwnerDunsNbr] = publisher

            p = random.random()
            if(p <= self.probabilityDifferentOwner):
                d.data[Data.factualOwnerDunsNbr] = random.randint(3e9,3e9+self.dunsRange)
            else:
                d.data[Data.factualOwnerDunsNbr] = publisher
                pass
            p = random.random()
            
            if (p <= self.probabilityGlobal):
                d.data[Data.globalUltimateDunsNbr] = d.data[Data.dunsNbr]
                pass
            else:
                d.data[Data.globalUltimateDunsNbr] = "null"
            d.data[Data.oprgStCd] = "o"
            d.data[Data.hierCode] = "h"
            d.data[Data.busName] = "b"
            d.data[Data.trdgNme1] = "t1"
            d.data[Data.trdgNme2] = "t2"
            d.data[Data.physAdrStrAdrLine] = "p1"
            d.data[Data.physAdrPostTown] = "pt"
            d.data[Data.physAdrPostCode] = "pc"
            d.data[Data.wbCtry] = "w"
            d.data[Data.physAdrStrAdrLine2] = "p2"
            self.write(d)
            pass
        print "Data file (%s) generated with (%d) size"%(self.file.name,count)
        pass



DefaultSize = 25000
DefaultOutput = "data.csv"
DefaultSeed = 0
DefaultDunsRange=1e6
DefaultProbGlobal=0.1
DefaultProbDifferentOwner=0.5
    
def usage():
    print "Generates fake GBR data"
    print "-h|--help to get this message"
    print "-s<integer>|--size=<integer> (default=%d) the number of items to generate"%(DefaultSize)
    print "-o<string> |--output=<string> (default=%s) the name of the generated data file"%(DefaultOutput)
    print "-r<integer>|--seed=<integer> (default=%d) random generator seed"%(DefaultSeed)
    print "-d<integer>|--duns=<integer> (default=%d) number of different DUNS"%(DefaultDunsRange)
    print "-g<integer>|--pglobal=<float> (default=%f) range (0 <= g <= 1) 0:no global, 1:always global"%(DefaultProbGlobal)
    print "-p<float>  |--pdiffowner=<float> (default=%f),range(0 <= p <= 1) 0:always same owner, 1:always different owner"%(DefaultProbDifferentOwner)
    pass

def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hs:o:r:d:g:p:", ["help","size=","output=","seed=","duns=","pglobal=","pdiffowner="])
    except getopt.GetoptError, err:
        print str(err) 
        usage()
        sys.exit(2)
        pass
    size = DefaultSize
    output = DefaultOutput
    seed = DefaultSeed
    duns = DefaultDunsRange
    pglobal = DefaultProbGlobal
    pdiffOwner = DefaultProbDifferentOwner
    for o, a in opts:
        if o in ("-h", "--help"):
            usage()
            sys.exit()
        elif o in ("-s","--size"):
            size = int(a)
        elif o in ("-o","--output"):
            output = a
        elif o in ("-r","--seed"):
            seed = int(a)
        elif o in ("-d","--duns"):
            duns = int(a)
        elif o in ("-g","--pglobal"):
            pglobal = float(a)
        elif o in ("-p","--pdiffowner"):
            pdiffOwner = float(a)
        else:
            assert False, "unhandled option"
            pass
        pass
    
    generator = DataGenerator(output,seed,duns,pglobal,pdiffOwner)
    generator.generate(size)
    pass

if __name__ == "__main__":
    main()
    



