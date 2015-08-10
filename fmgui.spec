#
# spec file for package fmgui
#
# Copyright (c) 2015 Intel Corporation
#
%define name IntelOPA-FMGUI
%define appdir IntelOPA-FMGUI-10_0_0_0_190
%define appfolder fmgui
%define appjar fmgui-10_0_0_0_190.jar
%define _binary_payload w9.gzdio

Name: IntelOPA-FMGUI
Version: 10_0_0_0_190
Release: beta
Summary: Fabric Manager Graphical User Interface
Group: Applications/System
ExclusiveArch: noarch
BuildArch: noarch
Source0: IntelOPA-FMGUI-10_0_0_0_190.source.tar 
Buildroot: %{_topdir}/%{name}-%{version}-buildroot
URL: www.intel.com
License: TBD

%description
FMGUI is the Fabric Manager Graphical User Interface.  It can be run by invoking the Bash
script fmgui.

%pre
getent group fmgui >/dev/null || groupadd -r fmgui
getent passwd fmgui >/dev/null || \
    useradd -r -g fmgui -s /sbin/nologin \
    -c "Owner for FM GUI jars and files" fmgui
exit 0

%prep
%setup -c -q

%build
echo "JAVA_HOME:               " $JAVA_HOME
echo "ANT_HOME:                " $ANT_HOME
PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH
export PATH
echo "PATH:                    " $PATH
cd %{name}-%{version}
ant -v build
if  [ -e "%{_topdir}/%{appjar}" ]; then
    cp %{_topdir}/%{appjar} .
fi

%install
rm -rf %{buildroot}
mkdir -p %{buildroot}%{_javadir}/%{appfolder}
mkdir -p %{buildroot}%{_javadir}/%{appfolder}/lib
mkdir -p %{buildroot}%{_javadir}/%{appfolder}/help
mkdir -p %{buildroot}/usr/local/bin
mkdir -p %{buildroot}/usr/local/share/applications
mkdir -p %{buildroot}/usr/local/share/desktop-directories
mkdir -p %{buildroot}/usr/local/share/icons/hicolor/48x48/apps
mkdir -p %{buildroot}/etc/xdg/menus/applications-merged
cp -a %{appdir}/%{appjar} %{buildroot}%{_javadir}/%{appfolder}
cp -a %{appdir}/lib/* %{buildroot}%{_javadir}/%{appfolder}/lib
cp -a %{appdir}/help/* %{buildroot}%{_javadir}/%{appfolder}/help
cp -a %{appdir}/install/fmgui.sh %{buildroot}/usr/local/bin/fmgui
cp -a %{appdir}/install/fmgui.desktop %{buildroot}/usr/local/share/applications
cp -a %{appdir}/install/Fabric.directory %{buildroot}/usr/local/share/desktop-directories
cp -a %{appdir}/install/images/* %{buildroot}/usr/local/share/icons/hicolor
cp -a %{appdir}/install/Fabric.menu %{buildroot}/etc/xdg/menus/applications-merged

%files
%defattr(755,fmgui,fmgui)
%{_javadir}/%{appfolder}
%attr(755,fmgui,fmgui) /usr/local/bin/fmgui
%attr(644,fmgui,fmgui) /usr/local/share/applications/fmgui.desktop
%attr(644,fmgui,fmgui) /usr/local/share/desktop-directories/Fabric.directory
/usr/local/share/icons/hicolor
%attr(644,fmgui,fmgui) %{_sysconfdir}/xdg/menus/applications-merged/Fabric.menu
