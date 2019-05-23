## bxm-svn-util
- BankwareGlobal 소프트웨어 연구소 인프라솔루션실 1팀 에서 사용하는 SVN Util
- The TMate Open Source License 가지는 svnkit 1.9.3 jar 를 사용기에 오픈소스로 공개 함. (https://svnkit.com/license.html)

## How to get source code
1. git clone https://github.com/bxmgithub/bxm-svn-util.git
2. download on site (https://github.com/bxmgithub/bxm-svn-util)
## Build

### JDK requirements

- jdk 1.6 or later

### Gradle
bxm-svn-util can be built by using gradle. After downloading the source, execute the following commands.

- For Linux, Unix
  - ./gradlew clean
  - ./gradlew build

- For Windows
  - gradlew.bat clean
  - gradlew.bat build
  
### License
The TMate Open Source License.

This license applies to all portions of TMate SVNKit library, which 
are not externally-maintained libraries (e.g. Ganymed SSH library).

All the source code and compiled classes in package org.tigris.subversion.javahl
except SvnClient class are covered by the license in JAVAHL-LICENSE file

Copyright (c) 2004-2012 TMate Software. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, 
      this list of conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, 
      this list of conditions and the following disclaimer in the documentation 
      and/or other materials provided with the distribution.
      
    * Redistributions in any form must be accompanied by information on how to 
      obtain complete source code for the software that uses SVNKit and any 
      accompanying software that uses the software that uses SVNKit. The source 
      code must either be included in the distribution or be available for no 
      more than the cost of distribution plus a nominal fee, and must be freely 
      redistributable under reasonable conditions. For an executable file, complete 
      source code means the source code for all modules it contains. It does not 
      include source code for modules or files that typically accompany the major 
      components of the operating system on which the executable file runs.
      
    * Redistribution in any form without redistributing source code for software 
      that uses SVNKit is possible only when such redistribution is explictly permitted 
      by TMate Software. Please, contact TMate Software at support@svnkit.com to 
      get such permission.

THIS SOFTWARE IS PROVIDED BY TMATE SOFTWARE ``AS IS'' AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT, ARE 
DISCLAIMED. 

IN NO EVENT SHALL TMATE SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
