## Measurements

The system was run on the designated server, using the [Griewank function](https://www.sfu.ca/~ssurjano/griewank.html), with 4,8 and 16 nodes; with the following parameters:

```json
{
  "data": [
    [-600, 600, 0.2, 5],
    [-600, 600, 0.2, 5],
    [-600, 600, 0.2, 5]
  ],
  "agg": "MIN",
  "maxItemsPerBatch": 10800000
}
```

### Average Summary

#### FaMAF Server

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
|---------------------|---------------------|---------------------|---------------------|
| Worker Throughput   | 0.64 Results/Second | 0.63 Results/Second | 0.6 Results/Second  |
| Combined Throughput | 2.57 Results/Second | 4.85 Results/Second | 9.47 Results/Second |
| Work-time Variation | 1.57%               | 1.95%               | 1.83%               |
| Memory Usage        | 370 MB/Worker       | 367 MB/Worker       | 360 MB/Worker       |
| Network Usage (Tx)  | 352 B/(s * Worker)  | 332 B/(s * Worker)  | 322 B/(s * Worker)  |
| Network Usage (Rx)  | 195 B/(s * Worker)  | 184 B/(s * Worker)  | 179 B/(s * Worker)  |
| CPU Usage           | 100%/Worker         | 100%/Worker         | 100%/Worker         |
| Completion Time     | 155 Minutes         | 82.2Minutes         | 42.1 Minutes        |

#### Cloud (GCP)

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
|---------------------|---------------------|---------------------|---------------------|
| Worker Throughput   | 0.51 Results/Second | 0.51 Results/Second | 0.52 Results/Second |
| Combined Throughput | 2.03 Results/Second | 4.12 Results/Second | 8.40 Results/Second |
| Work-time Variation | 3.16%               | 5.04%               | 6.19%               |
| Memory Usage        | 52-64 MB/Worker     | 52-60 MB/Worker     | 52-58 MB/Worker     |
| Network Usage (Tx)  | 276 B/(s * Worker)  | 281 B/(s * Worker)  | 285 B/(s * Worker)  |
| Network Usage (Rx)  | 153 B/(s * Worker)  | 156 B/(s * Worker)  | 159 B/(s * Worker)  |
| CPU Usage           | 100%/Worker         | 100%/Worker         | 100%/Worker         |
| Completion Time     | 196.8 Minutes       | 97.2 Minutes        | 47.7 Minutes        |
