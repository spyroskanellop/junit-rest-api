package com.example.junitrestapi;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationIT {

    @LocalServerPort
    private Integer port;

    @LocalManagementPort
    private Integer managementPort;

    @Test
    void printPortsInUse() {
        System.out.println(port);
        System.out.println(managementPort);
    }
}
