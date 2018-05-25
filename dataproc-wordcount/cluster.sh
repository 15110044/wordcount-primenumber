#!/bin/bash
#    Copyright 2015 Google, Inc.
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.

DEFAULT_ZONE='us-central1-b' 
DEFAULT_CLUSTER='dp'
=> DEFAULT_ZONE="asia-south1-a"
    DEFAULT_CLUSTER='wordcount-isprimenumber-cluster'

create) # create <bucket> [<clusterName> [zone]]
if (( $ < 2 )); then 
print_usage 
exit 
fi 
ZONE=${4:-$DEFAULT_ZONE} 
CLUSTER='${3:-$DEFAULT_CLUSTER}' 

gcloud dataproc clusters create '${CLUSTER}' \\ 
--bucket '$2' \\ 
--num-workers 4 \\ 
--zone $ZONE \\ 
--master-machine-type n1-standard-4 \\ 
--worker-machine-type n1-standard-4 
;;
=> 
create)   # create <bucket>

 if (( $# < 2 )); then
   print_usage
   exit
 fi
 ZONE=${4:-$DEFAULT_ZONE}
 CLUSTER="${3:-$DEFAULT_CLUSTER}"

 gcloud dataproc clusters create "${CLUSTER}" \
   --bucket "$2" \
   --num-workers 3 \
   --zone $ZONE \
   --master-machine-type n1-standard-2 \
   --worker-machine-type n1-standard-2
 ;;

start)  # start [<clusterName>]

  CLUSTER="${2:-$DEFAULT_CLUSTER}"

  TARGET="WordCount-$(date +%s)"
  gcloud dataproc jobs submit hadoop --cluster "$CLUSTER" \
    --jar target/wordcount-mapreduce-0-SNAPSHOT-jar-with-dependencies.jar \
    -- wordcount-hbase \
    gs://lesv-big-public-data/books/book \
    gs://lesv-big-public-data/books/b10 \
    gs://lesv-big-public-data/books/b100 \
    gs://lesv-big-public-data/books/b1232 \
  gs://lesv-big-public-data/books/b6130 \    "${TARGET}"
    echo "Output table is: ${TARGET}"
  ;;
=>



start)  # start [<clusterName>]

 CLUSTER="${2:-$DEFAULT_CLUSTER}"

 TARGET="WordCount-$(date +%s)"
 gcloud dataproc jobs submit hadoop --cluster "$CLUSTER" \
   --jar target/wordcount-mapreduce-0-SNAPSHOT-jar-with-dependencies.jar \
   -- wordcount-hbase \
   gs://wordcount-isprimenumber-bucket/input/input.txt \
   gs://wordcount-isprimenumber-bucket/output \
   "${TARGET}"
   echo "Output table is: ${TARGET}"
 ;;

esac
