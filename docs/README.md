# Benchmarks

## Measurements

### FaMAF Server

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
|---------------------|---------------------|---------------------|---------------------|
| Worker Throughput   | 0.64 Results/Second | 0.63 Results/Second | 0.6 Results/Second  |
| Combined Throughput | 2.57 Results/Second | 4.85 Results/Second | 9.42 Results/Second |
| Work-time Variation | 1.57%               | 1.95%               | 1.83%               |
| Memory Usage        | 370 MB/Worker       | 367 MB/Worker       | 360 MB/Worker       |
| CPU Usage           | 100%/Worker         | 100%/Worker         | 100%/Worker         |
| Completion Time     | 155 Minutes         | 82.2Minutes         | 42.1Minutes         |

### Cloud (GCP)

| Measurement         | 4 Nodes        | 8 Nodes        | 16 Nodes       |
|---------------------|----------------|----------------|----------------|
| Worker Throughput   | Results/Second | Results/Second | Results/Second |
| Combined Throughput | Results/Second | Results/Second | Results/Second |
| Work-time Variation | %              | %              | %              |
| Memory Usage        | MB/Worker      | MB/Worker      | MB/Worker      |
| CPU Usage           | %/Worker       | %/Worker       | %/Worker       |
| Completion Time     | Minutes        | Minutes        | Minutes        |

Average measurements using the [specified configuration](measurements/server/README.md)

## Subjective analysis

### Ease of development

Scala has a fast learning curve, and is very expressive.
The high level abstractions and garbage collection simplify development greatly.
Futures and promises are a great way to handle asynchronous code.
SBT provides an easy way to manage dependencies and build the project.

### External Resources

Scala is capable of using Java libraries, which opens up a huge amount of resources.
The compatibility with libraries is not always perfect, but it is usually possible to find a workaround.
Scala is commonly used with [Akka](https://akka.io/), an actor model library used for concurrent and distributed systems; we've been unable to use it effectively due to clustering, building and other issues.
