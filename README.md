A geofence is defined in an application. When the user enters this area or leaves, the following will be recorded in a database on the device: {lat, lon, action, timestamp}. The implementation was done using an activity that will start the process and a brodacast receiver. A content provider is also implemented to access data in third-party applications.

A second application will be able to access this data through a content provider of the first and display it on a map.
