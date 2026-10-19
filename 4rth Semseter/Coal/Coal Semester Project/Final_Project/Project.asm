INCLUDE Irvine32.inc

; =================================================================
; COAL SEMESTER PROJECT: REAL-TIME DIGITAL CLOCK & STOPWATCH
; Developer: Wasif Ali (Solo Task)
; Instructor: Sir Imran
; Description: 7-Segment UI, Live Date, Stopwatch, and 12/24hr Toggle
; =================================================================

.data
         sysTime     SYSTEMTIME <>

         ; --- VISUAL SETTINGS ---
         blockChar   BYTE '#'
         baseX       BYTE ?
         baseY       BYTE 8
         colorVal    BYTE 10                                                           ; Defaulting to Light Green (10) to match your screenshot
         lastSecond  BYTE 255
         needsClear  BYTE 1                                                            ; Flag to redraw screen cleanly when switching modes

         ; --- STATE MACHINE VARIABLES ---
         currentMode BYTE 0                                                            ; 0 = Clock, 1 = Stopwatch, 2 = About
         is12Hour    BYTE 0                                                            ; 0 = 24hr, 1 = 12hr
         isPM        BYTE 0                                                            ; 0 = AM, 1 = PM

         ; --- STOPWATCH VARIABLES ---
         swState     BYTE 0
         timeStart   DWORD 0
         timeElapsed DWORD 0
         swMins      DWORD 0
         swSecs      DWORD 0
         swCentis    DWORD 0

         ; --- UI STRINGS ---
         headerStr   BYTE "=================================================", 0
         titleStr    BYTE "   SEMESTER PROJECT: MULTI-FUNCTION TIME UI    ", 0
         authorStr   BYTE "   Subject: COAL | Developer: Wasif Ali        ", 0
         slashStr    BYTE " / ", 0
         strAM       BYTE " AM ", 0
         strPM       BYTE " PM ", 0

         ; --- ABOUT SCREEN STRINGS ---
         msgAbout1   BYTE "              COAL SEMESTER PROJECT              ", 0
         msgAbout2   BYTE "   Course: Computer Organization & Assembly      ", 0
         msgAbout3   BYTE "   Instructor: Sir Imran                         ", 0
         msgAbout4   BYTE "   Developer: Wasif Ali (Solo Task)              ", 0
         msgAbout5   BYTE "   Institution: Air University                   ", 0

         ; --- FOOTER MESSAGES ---
         msgClock    BYTE " MODE: CLOCK      [T] 12/24hr  [S] StopW  [A] About ", 0
         msgStopW    BYTE " MODE: STOPWATCH  [SPACE] Start/Stop  [R] Reset     ", 0
         msgAboutM   BYTE " MODE: ABOUT      [C] Clock   [S] Stopwatch         ", 0
         msgReturn   BYTE " [C] Return to Clock                                ", 0
         clearLine   BYTE "                                                    ", 0

.code
main PROC
                       call   Clrscr

    ; =========================================================
    ; MAIN APPLICATION LOOP
    ; =========================================================
    MainLoop:
                       call   HandleInput

                       ; Fetch current time
                       INVOKE GetLocalTime, ADDR sysTime

                       ; Check if we need to cleanly redraw the screen due to a mode change
                       cmp    needsClear, 1
                       jne    SkipClear
                       call   Clrscr
                       call   DrawUIHeader
                       mov    needsClear, 0
    SkipClear:

                       ; Only cycle color once per real second
                       movzx  eax, sysTime.wSecond
                       cmp    al, lastSecond
                       je     DrawActiveMode
                       mov    lastSecond, al

                       ; Conditional Color Cycling (Pause color if Stopwatch is paused)
                       cmp    currentMode, 1
                       jne    CycleColor
                       cmp    swState, 0
                       je     ApplyColor

    CycleColor:
                       inc    colorVal
                       cmp    colorVal, 15
                       jbe    ApplyColor
                       mov    colorVal, 9                      ; Cycle starts over
    ApplyColor:
                       movzx  eax, colorVal
                       call   SetTextColor

    DrawActiveMode:
                       cmp    currentMode, 0
                       je     ModeClock
                       cmp    currentMode, 1
                       je     ModeStopwatch
                       jmp    ModeAbout

    ; ---------------------------------------------------------
    ; MODE 0: REAL-TIME CLOCK + DATE
    ; ---------------------------------------------------------
    ; ---------------------------------------------------------
    ; MODE 0: REAL-TIME CLOCK + DATE
    ; ---------------------------------------------------------
    ModeClock:
                       movzx  eax, sysTime.wHour

                       ; --- WINE ENVIRONMENT FIX ---
                       ; Manually add 12 hours because Wine is stuck in AM
                       add    eax, 12
                       cmp    eax, 24
                       jb     TimeFixed
                       sub    eax, 24                          ; Wrap around if it goes over 23
    TimeFixed:

                       cmp    is12Hour, 1
                       jne    DrawHoursNormal

                       ; --- BULLETPROOF 12-HOUR MATH ---
                       mov    isPM, 0                          ; Default to AM
                       cmp    eax, 12
                       jb     CheckMidnight                    ; If less than 12, it is AM

                       mov    isPM, 1                          ; If we reach here, it is PM
                       cmp    eax, 12                          ; Is it exactly 12 Noon?
                       je     DrawHoursNormal                  ; If yes, leave it as 12
                       sub    eax, 12                          ; Otherwise, subtract 12 (e.g., 19 becomes 7)
                       jmp    DrawHoursNormal

    CheckMidnight:
                       cmp    eax, 0                           ; Is it exactly 00:00 (Midnight)?
                       jne    DrawHoursNormal                  ; If 1-11 AM, leave it alone
                       mov    eax, 12                          ; Convert 0 to 12 AM

    DrawHoursNormal:
                       ; Draw Hours
                       mov    bl, 10
                       div    bl
                       mov    dl, 10
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 20
                       movzx  eax, ah
                       call   DrawHugeDigit

                       ; Draw Colon 1
                       mov    dl, 30
                       call   DrawColon

                       ; Draw Minutes
                       movzx  eax, sysTime.wMinute
                       mov    bl, 10
                       div    bl
                       mov    dl, 35
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 45
                       movzx  eax, ah
                       call   DrawHugeDigit

                       ; Draw Colon 2
                       mov    dl, 55
                       call   DrawColon

                       ; Draw Seconds
                       movzx  eax, sysTime.wSecond
                       mov    bl, 10
                       div    bl
                       mov    dl, 60
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 70
                       movzx  eax, ah
                       call   DrawHugeDigit

                       ; Draw AM/PM Text (if 12-Hour Mode is active)
                       cmp    is12Hour, 1
                       jne    DrawDate
                       mov    eax, white + (black * 16)
                       call   SetTextColor
                       mov    dl, 77
                       mov    dh, 13
                       call   Gotoxy
                       cmp    isPM, 1
                       je     PrintPM
                       mov    edx, OFFSET strAM
                       call   WriteString
                       jmp    DrawDate
    PrintPM:
                       mov    edx, OFFSET strPM
                       call   WriteString

    DrawDate:
                       ; Draw Real Date
                       mov    eax, white + (black * 16)
                       call   SetTextColor
                       mov    dl, 33
                       mov    dh, 18
                       call   Gotoxy
                       movzx  eax, sysTime.wDay
                       call   WriteZeroPadded
                       mov    edx, OFFSET slashStr
                       call   WriteString
                       movzx  eax, sysTime.wMonth
                       call   WriteZeroPadded
                       call   WriteString
                       movzx  eax, sysTime.wYear
                       call   WriteDec

                       ; Draw Clock Footer
                       mov    dl, 12
                       mov    dh, 21
                       call   Gotoxy
                       mov    edx, OFFSET msgClock
                       call   WriteString
                       mov    dl, 12
                       mov    dh, 22
                       call   Gotoxy
                       mov    edx, OFFSET clearLine
                       call   WriteString

                       jmp    LoopDelay

    ; ---------------------------------------------------------
    ; MODE 1: STOPWATCH (MM:SS:CC)
    ; ---------------------------------------------------------
    ModeStopwatch:
                       cmp    swState, 1
                       jne    CalcStopped
                       call   GetMSeconds
                       sub    eax, timeStart
                       add    eax, timeElapsed
                       jmp    DoMath
    CalcStopped:
                       mov    eax, timeElapsed

    DoMath:
                       mov    edx, 0
                       mov    ebx, 60000
                       div    ebx
                       mov    swMins, eax

                       mov    eax, edx
                       mov    edx, 0
                       mov    ebx, 1000
                       div    ebx
                       mov    swSecs, eax

                       mov    eax, edx
                       mov    edx, 0
                       mov    ebx, 10
                       div    ebx
                       mov    swCentis, eax

                       movzx  eax, colorVal
                       call   SetTextColor

                       ; Mins
                       mov    eax, swMins
                       mov    bl, 10
                       div    bl
                       mov    dl, 10
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 20
                       movzx  eax, ah
                       call   DrawHugeDigit

                       mov    dl, 30
                       call   DrawColon

                       ; Secs
                       mov    eax, swSecs
                       mov    bl, 10
                       div    bl
                       mov    dl, 35
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 45
                       movzx  eax, ah
                       call   DrawHugeDigit

                       mov    dl, 55
                       call   DrawColon

                       ; Centis
                       mov    eax, swCentis
                       mov    bl, 10
                       div    bl
                       mov    dl, 60
                       push   eax
                       movzx  eax, al
                       call   DrawHugeDigit
                       pop    eax
                       mov    dl, 70
                       movzx  eax, ah
                       call   DrawHugeDigit

                       ; Draw Stopwatch Footer
                       mov    eax, white + (black * 16)
                       call   SetTextColor
                       mov    dl, 12
                       mov    dh, 18
                       call   Gotoxy
                       mov    edx, OFFSET clearLine
                       call   WriteString

                       mov    dl, 12
                       mov    dh, 21
                       call   Gotoxy
                       mov    edx, OFFSET msgStopW
                       call   WriteString
                       mov    dl, 12
                       mov    dh, 22
                       call   Gotoxy
                       mov    edx, OFFSET msgReturn
                       call   WriteString

                       jmp    LoopDelay

    ; ---------------------------------------------------------
    ; MODE 2: ABOUT / CREDITS SCREEN
    ; ---------------------------------------------------------
    ModeAbout:
                       movzx  eax, colorVal
                       call   SetTextColor

                       mov    dl, 15
                       mov    dh, 9
                       call   Gotoxy
                       mov    edx, OFFSET msgAbout1
                       call   WriteString

                       mov    eax, white + (black * 16)
                       call   SetTextColor

                       mov    dl, 15
                       mov    dh, 11
                       call   Gotoxy
                       mov    edx, OFFSET msgAbout2
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 13
                       call   Gotoxy
                       mov    edx, OFFSET msgAbout3
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 15
                       call   Gotoxy
                       mov    edx, OFFSET msgAbout4
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 17
                       call   Gotoxy
                       mov    edx, OFFSET msgAbout5
                       call   WriteString

                       ; About Footer
                       mov    dl, 12
                       mov    dh, 21
                       call   Gotoxy
                       mov    edx, OFFSET msgAboutM
                       call   WriteString
                       mov    dl, 12
                       mov    dh, 22
                       call   Gotoxy
                       mov    edx, OFFSET clearLine
                       call   WriteString

    LoopDelay:
                       mov    eax, 50
                       call   Delay
                       jmp    MainLoop

QuitProg::
                       mov    eax, lightGray + (black * 16)
                       call   SetTextColor
                       call   Clrscr
                       exit
main ENDP

; =================================================================
; PROCEDURE: Draw UI Header
; =================================================================
DrawUIHeader PROC
                       mov    eax, white + (black * 16)
                       call   SetTextColor
                       mov    dl, 15
                       mov    dh, 1
                       call   Gotoxy
                       mov    edx, OFFSET headerStr
                       call   WriteString
                       mov    dl, 15
                       mov    dh, 2
                       call   Gotoxy
                       mov    edx, OFFSET titleStr
                       call   WriteString
                       mov    dl, 15
                       mov    dh, 3
                       call   Gotoxy
                       mov    edx, OFFSET authorStr
                       call   WriteString
                       mov    dl, 15
                       mov    dh, 4
                       call   Gotoxy
                       mov    edx, OFFSET headerStr
                       call   WriteString
                       ret
DrawUIHeader ENDP

; =================================================================
; INPUT HANDLER (State Machine Controller)
; =================================================================
HandleInput PROC
                       call   ReadKey
                       jz     NoInput

                       cmp    al, 27
                       je     QuitProg

                       cmp    al, 'c'
                       je     SwitchToClock
                       cmp    al, 'C'
                       je     SwitchToClock

                       cmp    al, 's'
                       je     SwitchToStopwatch
                       cmp    al, 'S'
                       je     SwitchToStopwatch

                       cmp    al, 'a'
                       je     SwitchToAbout
                       cmp    al, 'A'
                       je     SwitchToAbout

                       cmp    al, 't'
                       je     Toggle12Hour
                       cmp    al, 'T'
                       je     Toggle12Hour

                       cmp    al, ' '
                       je     ToggleStopwatch

                       cmp    al, 'r'
                       je     ResetStopwatch
                       cmp    al, 'R'
                       je     ResetStopwatch
                       jmp    NoInput

    SwitchToClock:
                       cmp    currentMode, 0
                       je     NoInput
                       mov    currentMode, 0
                       mov    needsClear, 1
                       ret
    SwitchToStopwatch:
                       cmp    currentMode, 1
                       je     NoInput
                       mov    currentMode, 1
                       mov    needsClear, 1
                       ret
    SwitchToAbout:
                       cmp    currentMode, 2
                       je     NoInput
                       mov    currentMode, 2
                       mov    needsClear, 1
                       ret
    Toggle12Hour:
                       cmp    currentMode, 0                   ; Only allow toggle if in Clock mode
                       jne    NoInput
                       xor    is12Hour, 1
                       mov    needsClear, 1                    ; Clear to cleanly remove/draw AM PM text
                       ret
    ToggleStopwatch:
                       cmp    currentMode, 1
                       jne    NoInput
                       xor    swState, 1
                       cmp    swState, 1
                       jne    Pausing
                       call   GetMSeconds
                       mov    timeStart, eax
                       ret
    Pausing:
                       call   GetMSeconds
                       sub    eax, timeStart
                       add    timeElapsed, eax
                       ret
    ResetStopwatch:
                       cmp    currentMode, 1
                       jne    NoInput
                       mov    swState, 0
                       mov    timeElapsed, 0
                       mov    timeStart, 0
                       ret
    NoInput:
                       ret
HandleInput ENDP

; =================================================================
; HELPER: ZERO-PADDED NUMBER PRINTING
; =================================================================
WriteZeroPadded PROC
                       cmp    eax, 10
                       jae    PrintNumber
                       push   eax
                       mov    al, '0'
                       call   WriteChar
                       pop    eax
    PrintNumber:
                       call   WriteDec
                       ret
WriteZeroPadded ENDP

; =================================================================
; 7-SEGMENT RENDERING ENGINE (Using ASCII Blocks)
; =================================================================
DrawHugeDigit PROC
                       mov    baseX, dl
                       push   eax
                       call   ClearDigitArea
                       pop    eax

                       cmp    eax, 0
                       jne    chk1
                       call   SegA
                       call   SegB
                       call   SegC
                       call   SegD
                       call   SegE
                       call   SegF
                       ret

    chk1:              cmp    eax, 1
                       jne    chk2
                       call   SegB
                       call   SegC
                       ret

    chk2:              cmp    eax, 2
                       jne    chk3
                       call   SegA
                       call   SegB
                       call   SegG
                       call   SegE
                       call   SegD
                       ret

    chk3:              cmp    eax, 3
                       jne    chk4
                       call   SegA
                       call   SegB
                       call   SegC
                       call   SegD
                       call   SegG
                       ret

    chk4:              cmp    eax, 4
                       jne    chk5
                       call   SegF
                       call   SegG
                       call   SegB
                       call   SegC
                       ret

    chk5:              cmp    eax, 5
                       jne    chk6
                       call   SegA
                       call   SegF
                       call   SegG
                       call   SegC
                       call   SegD
                       ret

    chk6:              cmp    eax, 6
                       jne    chk7
                       call   SegA
                       call   SegF
                       call   SegE
                       call   SegD
                       call   SegC
                       call   SegG
                       ret

    chk7:              cmp    eax, 7
                       jne    chk8
                       call   SegA
                       call   SegB
                       call   SegC
                       ret

    chk8:              cmp    eax, 8
                       jne    chk9
                       call   SegA
                       call   SegB
                       call   SegC
                       call   SegD
                       call   SegE
                       call   SegF
                       call   SegG
                       ret

    chk9:
                       call   SegA
                       call   SegB
                       call   SegC
                       call   SegD
                       call   SegF
                       call   SegG
                       ret
DrawHugeDigit ENDP

; --- Individual Segments ---
SegA PROC
                       mov    dl, baseX
                       add    dl, 1
                       mov    dh, baseY
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       ret
SegA ENDP

SegB PROC
                       mov    dl, baseX
                       add    dl, 5
                       mov    dh, baseY
                       add    dh, 1
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       inc    dh
                       call   Gotoxy
                       call   WriteChar
                       ret
SegB ENDP

SegC PROC
                       mov    dl, baseX
                       add    dl, 5
                       mov    dh, baseY
                       add    dh, 4
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       inc    dh
                       call   Gotoxy
                       call   WriteChar
                       ret
SegC ENDP

SegD PROC
                       mov    dl, baseX
                       add    dl, 1
                       mov    dh, baseY
                       add    dh, 6
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       ret
SegD ENDP

SegE PROC
                       mov    dl, baseX
                       mov    dh, baseY
                       add    dh, 4
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       inc    dh
                       call   Gotoxy
                       call   WriteChar
                       ret
SegE ENDP

SegF PROC
                       mov    dl, baseX
                       mov    dh, baseY
                       add    dh, 1
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       inc    dh
                       call   Gotoxy
                       call   WriteChar
                       ret
SegF ENDP

SegG PROC
                       mov    dl, baseX
                       add    dl, 1
                       mov    dh, baseY
                       add    dh, 3
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       ret
SegG ENDP

DrawColon PROC
                       mov    dh, baseY
                       add    dh, 2
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       mov    dh, baseY
                       add    dh, 4
                       call   Gotoxy
                       mov    al, blockChar
                       call   WriteChar
                       ret
DrawColon ENDP

ClearDigitArea PROC
                       push   eax
                       push   ecx
                       mov    dh, baseY
                       mov    ecx, 7
    clrLoop:
                       mov    dl, baseX
                       call   Gotoxy
                       mov    al, ' '
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       call   WriteChar
                       inc    dh
                       loop   clrLoop
                       pop    ecx
                       pop    eax
                       ret
ClearDigitArea ENDP

END main