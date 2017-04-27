how the jaccard algrithm runs
1. The main class is com.capstone.MainRun
2. The class ClusterDataService supports some method about Cluster Group
   such as BuildDataStructure that it can build a group list MAP between start date and end date.
   it is precondition that building a Map Data Structure before do anything,
   because this method will invoke K-means algorithm to create cluster groups and divide stock points to different groups.
   The number of groups has been define in the DataConstant.class
3.The class Jaccard contains two methods, 
  "similarityValue" method will calculate the similarity value of any two groups which includes stock points
  "coefficient" method will consider and calculate the coefficient of a StockPoint like S2 in different time windows.
  I tested data between 2016-12-28 and 2016-12-30, the coefficient of stock CHK was about 0.6.
  the result of testing means we can get Jaccard Index coefficient of a specific stock in a specific time window during a time series.
  for example,we want to know what is the coefficient of a Stock CHK which is in a specific day "2016-12-28" between "2016-12-28" and "2016-12-30"
  This could be that the Stock is abnormal data in "2016-12-29" time window.
  
  
It is complicated to understand how Jaccard formula is going.

   
