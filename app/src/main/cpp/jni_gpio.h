//
// Created by sooorajjj on 26/4/18.
//

#ifndef BLUEMOONGPIO_JNI_GPIO_H
#define BLUEMOONGPIO_JNI_GPIO_H

int read_gpio(char *path, void (*callback)(int));

#endif //BLUEMOONGPIO_JNI_GPIO_H
