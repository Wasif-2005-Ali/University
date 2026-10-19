INCLUDE c:\Users\Wasif Ali\.vscode\extensions\istareatscreens.masm-runner-0.9.1\native\irvine\Irvine32.inc

.data
    myName DB "Wasif Ali", 0    ;[cite: 1]

.code
main PROC
         ; Blue background (16) + Yellow text (14) = 30
         mov  eax, yellow + (blue * 16)
         call SetTextColor

         mov  edx, OFFSET myName
         call WriteString
         call crlf

         exit
main ENDP
END main